# halodb-native-demo

[![LICENSE](https://img.shields.io/github/license/rinx/halodb-native-demo)](https://github.com/rinx/halodb-native-demo/blob/master/LICENSE)

This is a sample repository to use [yahoo/HaloDB](https://github.com/yahoo/HaloDB) in GraalVM native image through [rinx/clj-halodb](https://github.com/rinx/clj-halodb).

HaloDB has two types of memory allocators, JNA and `sun.misc.Unsafe`.
SubstrateVM does not support JNA one. In this project, `sun.misc.Unsafe` memory allocator is used.
To use it, a system property should be set as below:

```clojure
(System/setProperty "org.caffinitas.ohc.allocator" "unsafe")
```

This line is defined in `halodb-native-demo.service.halodb` namespace.

