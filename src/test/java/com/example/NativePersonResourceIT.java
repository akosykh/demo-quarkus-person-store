package com.example;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativePersonResourceIT extends PersonResourceTest {

    // Execute the same tests but in native mode.
}