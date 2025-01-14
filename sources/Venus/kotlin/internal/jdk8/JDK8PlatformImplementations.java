/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package kotlin.internal.jdk8;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.internal.jdk7.JDK7PlatformImplementations;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.random.Random;
import kotlin.random.jdk8.PlatformThreadLocalRandom;
import kotlin.ranges.IntRange;
import kotlin.text.MatchGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002\u00a8\u0006\u0010"}, d2={"Lkotlin/internal/jdk8/JDK8PlatformImplementations;", "Lkotlin/internal/jdk7/JDK7PlatformImplementations;", "()V", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "sdkIsNullOrAtLeast", "", "version", "", "ReflectSdkVersion", "kotlin-stdlib-jdk8"})
public class JDK8PlatformImplementations
extends JDK7PlatformImplementations {
    private final boolean sdkIsNullOrAtLeast(int n) {
        return ReflectSdkVersion.sdkVersion == null || ReflectSdkVersion.sdkVersion >= n;
    }

    @Override
    @Nullable
    public MatchGroup getMatchResultNamedGroup(@NotNull MatchResult matchResult, @NotNull String string) {
        MatchGroup matchGroup;
        Intrinsics.checkNotNullParameter(matchResult, "matchResult");
        Intrinsics.checkNotNullParameter(string, "name");
        Matcher matcher = matchResult instanceof Matcher ? (Matcher)matchResult : null;
        if (matcher == null) {
            throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
        }
        Matcher matcher2 = matcher;
        IntRange intRange = new IntRange(matcher2.start(string), matcher2.end(string) - 1);
        if (intRange.getStart() >= 0) {
            String string2 = matcher2.group(string);
            Intrinsics.checkNotNullExpressionValue(string2, "matcher.group(name)");
            matchGroup = new MatchGroup(string2, intRange);
        } else {
            matchGroup = null;
        }
        return matchGroup;
    }

    @Override
    @NotNull
    public Random defaultPlatformRandom() {
        return this.sdkIsNullOrAtLeast(34) ? (Random)new PlatformThreadLocalRandom() : super.defaultPlatformRandom();
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0005\u00a8\u0006\u0006"}, d2={"Lkotlin/internal/jdk8/JDK8PlatformImplementations$ReflectSdkVersion;", "", "()V", "sdkVersion", "", "Ljava/lang/Integer;", "kotlin-stdlib-jdk8"})
    @SourceDebugExtension(value={"SMAP\nJDK8PlatformImplementations.kt\nKotlin\n*S Kotlin\n*F\n+ 1 JDK8PlatformImplementations.kt\nkotlin/internal/jdk8/JDK8PlatformImplementations$ReflectSdkVersion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,61:1\n1#2:62\n*E\n"})
    private static final class ReflectSdkVersion {
        @NotNull
        public static final ReflectSdkVersion INSTANCE;
        @JvmField
        @Nullable
        public static final Integer sdkVersion;

        private ReflectSdkVersion() {
        }

        static {
            Object object;
            Object object2;
            INSTANCE = new ReflectSdkVersion();
            try {
                object2 = Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
                object2 = object2 instanceof Integer ? (Integer)object2 : null;
            } catch (Throwable throwable) {
                object2 = null;
            }
            Object object3 = object2;
            if (object3 != null) {
                object2 = object3;
                int n = ((Number)object2).intValue();
                boolean bl = false;
                object = n > 0 ? object2 : null;
            } else {
                object = null;
            }
            sdkVersion = object;
        }
    }
}

