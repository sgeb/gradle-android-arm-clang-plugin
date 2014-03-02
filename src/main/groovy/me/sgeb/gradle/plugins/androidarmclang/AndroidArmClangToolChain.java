package me.sgeb.gradle.plugins.androidarmclang;

import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.nativebinaries.platform.Platform;
import org.gradle.nativebinaries.platform.internal.ArchitectureInternal;
import org.gradle.nativebinaries.toolchain.Clang;
import org.gradle.nativebinaries.toolchain.TargetPlatformConfiguration;
import org.gradle.nativebinaries.toolchain.internal.ToolType;
import org.gradle.nativebinaries.toolchain.internal.clang.ClangToolChain;
import org.gradle.process.internal.ExecActionFactory;

import java.util.List;

import static java.util.Collections.emptyList;

public class AndroidArmClangToolChain extends ClangToolChain implements AndroidArmClang {
    public static final String DEFAULT_NAME = "androidArmClang";

    public AndroidArmClangToolChain(String name, OperatingSystem operatingSystem, FileResolver fileResolver, ExecActionFactory execActionFactory) {
        super(name, operatingSystem, fileResolver, execActionFactory);

        addPlatformConfiguration(new ArmArchitecture());
        // Needed because AbstractGccCompatibleToolChain adds three
        // TargetPlatformConfiguration's by default and we must overwrite them
        addPlatformConfiguration(new PhonyArchitecture());
        addPlatformConfiguration(new PhonyArchitecture());

        tools.setExeName(ToolType.CPP_COMPILER, "arm-linux-androideabi-clang++");
        tools.setExeName(ToolType.C_COMPILER, "arm-linux-androideabi-clang");
        tools.setExeName(ToolType.OBJECTIVECPP_COMPILER, "arm-linux-androideabi-clang++");
        tools.setExeName(ToolType.OBJECTIVEC_COMPILER, "arm-linux-androideabi-clang");
        tools.setExeName(ToolType.ASSEMBLER, "arm-linux-androideabi-as");
        tools.setExeName(ToolType.LINKER, "arm-linux-androideabi-clang++");
        tools.setExeName(ToolType.STATIC_LIB_ARCHIVER, "arm-linux-androideabi-ar");
    }

    @Override
    protected String getTypeName() {
        return "AndroidArmClang";
    }

    private static class ArmArchitecture implements TargetPlatformConfiguration {
        public boolean supportsPlatform(Platform targetPlatform) {
            return targetPlatform.getOperatingSystem().isLinux()
                && targetPlatform.getName().toLowerCase().contains("android")
                && ((ArchitectureInternal) targetPlatform.getArchitecture()).isArm();
        }

        public List<String> getAssemblerArgs() {
            return emptyList();
        }

        public List<String> getCppCompilerArgs() {
            return emptyList();
        }

        public List<String> getCCompilerArgs() {
            return emptyList();
        }

        public List<String> getObjectiveCppCompilerArgs() {
            return emptyList();
        }

        public List<String> getObjectiveCCompilerArgs() {
            return emptyList();
        }

        public List<String> getStaticLibraryArchiverArgs() {
            return emptyList();
        }

        public List<String> getLinkerArgs() {
            return emptyList();
        }
    }

    private static class PhonyArchitecture extends ArmArchitecture {
        public boolean supportsPlatform(Platform targetPlatform) {
            return false;
        }
    }
}
