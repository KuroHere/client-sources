package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public abstract class StateMapperBase implements IStateMapper {
	protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.<IBlockState, ModelResourceLocation> newLinkedHashMap();

	public String getPropertyString(Map<IProperty<?>, Comparable<?>> values) {
		StringBuilder stringbuilder = new StringBuilder();

		for (Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
			if (stringbuilder.length() != 0) {
				stringbuilder.append(",");
			}

			IProperty<?> iproperty = entry.getKey();
			stringbuilder.append(iproperty.getName());
			stringbuilder.append("=");
			stringbuilder.append(this.getPropertyName(iproperty, (Comparable) entry.getValue()));
		}

		if (stringbuilder.length() == 0) {
			stringbuilder.append("normal");
		}

		return stringbuilder.toString();
	}

	private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> value) {
		return property.getName((T) value);
	}

	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
		for (IBlockState iblockstate : blockIn.getBlockState().getValidStates()) {
			this.mapStateModelLocations.put(iblockstate, this.getModelResourceLocation(iblockstate));
		}

		return this.mapStateModelLocations;
	}

	protected abstract ModelResourceLocation getModelResourceLocation(IBlockState state);
}
