package me.sgeb.gradle.plugins.androidarmclang

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.os.OperatingSystem
import org.gradle.model.ModelRule
import org.gradle.model.ModelRules
import org.gradle.internal.reflect.Instantiator
import org.gradle.nativebinaries.toolchain.internal.ToolChainRegistryInternal
import org.gradle.nativebinaries.plugins.NativeBinariesPlugin
import org.gradle.nativebinaries.toolchain.Clang
import org.gradle.nativebinaries.toolchain.internal.clang.ClangToolChain
import org.gradle.process.internal.ExecActionFactory

import javax.inject.Inject

class AndroidArmClangPlugin implements Plugin<Project> {
    private final FileResolver fileResolver
    private final ExecActionFactory execActionFactory
    private final Instantiator instantiator
    private final ModelRules modelRules

    @Inject
    AndroidArmClangPlugin(FileResolver fileResolver, ExecActionFactory execActionFactory, ModelRules modelRules, Instantiator instantiator) {
        this.execActionFactory = execActionFactory
        this.fileResolver = fileResolver
        this.instantiator = instantiator
        this.modelRules = modelRules
    }

    void apply(Project project) {
        project.plugins.apply(NativeBinariesPlugin)

        modelRules.rule(new ModelRule() {
            void addToolChain(ToolChainRegistryInternal toolChainRegistry) {
                toolChainRegistry.registerFactory(AndroidArmClang, { String name ->
                    return instantiator.newInstance(AndroidArmClangToolChain, name, OperatingSystem.LINUX, fileResolver, execActionFactory)
                })
                toolChainRegistry.registerDefaultToolChain(AndroidArmClangToolChain.DEFAULT_NAME, AndroidArmClang)
            }
        })
    }
}
