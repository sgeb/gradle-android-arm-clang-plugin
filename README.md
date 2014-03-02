# Gradle Android-Arm-Clang plugin

[![Build Status](https://travis-ci.org/sgeb/gradle-android-arm-clang-plugin.png?branch=master)](https://travis-ci.org/sgeb/gradle-android-arm-clang-plugin)

This Gradle plugin integrates the [Android NDK
toolchain](https://developer.android.com/tools/sdk/ndk/index.html) (ARM,
Clang-based) into Gradle to cross-compile C/C++ code. The standalone toolchain
must be installed separately, please refer to the Android documentation for
instructions.

## How?

*I will soon upload to MavenCentral for convenience. Still waiting for my
request to get approved.*

```groovy
executables {
    main {}
}

model {
    platforms {
        "android-arm" {
            operatingSystem "linux"
            architecture "arm"
        }
    }
}
```

Make sure you have `arm-linux-androideabi-clang++` from the Android toolchain in
your `PATH`.

There is an annoyance in the `bin` directory of the Android toolchain: it
contains binaries called `clang` and `clang++`. In order for these not to
override you system-wide compiler, add the toolchain `bin` directory at the end
of `PATH`.

Gradle does not know any OS called `android` and we must stick to `linux` in the
platform definition (which is technically correct). But the Android toolchain
cannot cross-compile to any arbitrary linux-arm platform, only to a platform
running the androideabi. The plugin checks the following to make sure it only
builds for the correct platform:

* operatingSystem must be `linux`
* architecture must be `arm`
* platform name (`android-arm` in this example) must contain the string `android`

## Multiple platforms in peaceful co-existence

The following example defines multiple platforms. They can be (cross-)compiled
using the same build folder and the same code base.

```groovy
executables {
    main {}
}

model {
    platforms {
        "osx-x64" {
            operatingSystem "mac os x"
            architecture "x86_64"
        }
        "android-arm" {
            operatingSystem "linux"
            architecture "arm"
        }
    }
}

task build {
    description "Build all binaries on all possible platforms."
    dependsOn binaries.matching { it.buildable }
}
```

The `tasks --all` command shows which platforms can be built with the configured
toolchains and the compilers found in the `PATH`.

```
> ./gradlew tasks --all
[...]
build - Build all binaries on all possible platforms. [android-armMainExecutable, osx-x64MainExecutable]
```

The same command without the Android toolchain in the `PATH`:

```
> ./gradlew tasks --all
[...]
build - Build all binaries on all possible platforms. [osx-x64MainExecutable]
```

If a platform/binary combination cannot be fulfilled, Gradle will explain why:

```
> ./gradlew android-armMainExecutable
[...]
:compileAndroid-armMainExecutableMainCpp FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileAndroid-armMainExecutableMainCpp'.
> No tool chain is available to build for platform 'android-arm':
    - Tool chain 'visualCpp' (Visual Studio): Visual Studio is not available on this operating system.
    - Tool chain 'gcc' (GNU GCC): XCode g++ is a wrapper around Clang. Treating it as Clang and not GCC.
    - Tool chain 'clang' (Clang): Don't know how to build for platform 'android-arm'.
    - Tool chain 'androidArmClang' (AndroidArmClang): Could not find C++ compiler 'arm-linux-androideabi-clang++' in system path.

BUILD FAILED
```

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
