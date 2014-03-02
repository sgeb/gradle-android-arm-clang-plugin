package me.sgeb.gradle.plugins.androidarmclang

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar
import org.gradle.nativebinaries.platform.PlatformContainer
import org.gradle.nativebinaries.toolchain.ToolChainRegistry
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertEquals

/**
 * JUnit test for {@link AndroidArmClangPlugin}.
 *
 * @author Serge Gebhardt
 */
class AndroidArmClangPluginTest {

    Project project

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'android-arm-clang'
    }

    @Test
    public void 'plugin applies native-binaries plugin'() {
        assertTrue project.plugins.hasPlugin('native-binaries')
    }

    @Test
    public void 'plugin adds AndroidArmClang toolchain'() {
        def toolChain = project.
            modelRegistry.get("toolChains", ToolChainRegistry).
            getByName("androidArmClang")

        assertNotNull toolChain
    }

    @Test
    public void 'plugin matches AndroidArmClang toolchain with android-arm platform'() {
        project.model {
            platforms {
                "android-arm" {
                    operatingSystem "linux"
                    architecture "arm"
                }
            }
        }

        def platform = project.modelRegistry.
            get("platforms", PlatformContainer).
            getByName("android-arm")

        assertNotNull platform

        def toolChain = project.modelRegistry.
            get("toolChains", ToolChainRegistry).
            getForPlatform(platform)

        assertNotNull toolChain
    }
}
