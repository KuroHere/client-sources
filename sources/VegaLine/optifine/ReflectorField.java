/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package optifine;

import java.lang.reflect.Field;
import optifine.FieldLocatorFixed;
import optifine.FieldLocatorName;
import optifine.FieldLocatorType;
import optifine.IFieldLocator;
import optifine.Reflector;
import optifine.ReflectorClass;

public class ReflectorField {
    private IFieldLocator fieldLocator = null;
    private boolean checked = false;
    private Field targetField = null;

    public ReflectorField(ReflectorClass p_i85_1_, String p_i85_2_) {
        this(new FieldLocatorName(p_i85_1_, p_i85_2_));
    }

    public ReflectorField(ReflectorClass p_i86_1_, String p_i86_2_, boolean p_i86_3_) {
        this(new FieldLocatorName(p_i86_1_, p_i86_2_), p_i86_3_);
    }

    public ReflectorField(ReflectorClass p_i87_1_, Class p_i87_2_) {
        this(p_i87_1_, p_i87_2_, 0);
    }

    public ReflectorField(ReflectorClass p_i88_1_, Class p_i88_2_, int p_i88_3_) {
        this(new FieldLocatorType(p_i88_1_, p_i88_2_, p_i88_3_));
    }

    public ReflectorField(Field p_i89_1_) {
        this(new FieldLocatorFixed(p_i89_1_));
    }

    public ReflectorField(IFieldLocator p_i90_1_) {
        this(p_i90_1_, false);
    }

    public ReflectorField(IFieldLocator p_i91_1_, boolean p_i91_2_) {
        this.fieldLocator = p_i91_1_;
        if (!p_i91_2_) {
            this.getTargetField();
        }
    }

    public Field getTargetField() {
        if (this.checked) {
            return this.targetField;
        }
        this.checked = true;
        this.targetField = this.fieldLocator.getField();
        if (this.targetField != null) {
            this.targetField.setAccessible(true);
        }
        return this.targetField;
    }

    public Object getValue() {
        return Reflector.getFieldValue(null, this);
    }

    public void setValue(Object p_setValue_1_) {
        Reflector.setFieldValue(null, this, p_setValue_1_);
    }

    public void setValue(Object p_setValue_1_, Object p_setValue_2_) {
        Reflector.setFieldValue(p_setValue_1_, this, p_setValue_2_);
    }

    public boolean exists() {
        return this.getTargetField() != null;
    }
}

