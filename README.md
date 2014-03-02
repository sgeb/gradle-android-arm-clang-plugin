# Gradle Android-Arm-Clang plugin

This Gradle plugin provides the Android-Arm-Clang toolchain to cross-compile
C/C++ code. The toolchain must be installed separately, please refer to the
Android documentation for instructions.

## How?

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
builts for the correct platform:

* operatingSystem must be `linux`
* architecture must be `arm`
* platform name (`android-arm` in this example) must contain the string `android`

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
