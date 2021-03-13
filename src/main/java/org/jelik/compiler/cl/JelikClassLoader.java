/*
 * Copyright 2019 Marcin Bukowiecki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jelik.compiler.cl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Marcin Bukowiecki
 */
public class JelikClassLoader extends URLClassLoader {

    private static final Logger log = Logger.getLogger(JelikClassLoader.class.getCanonicalName());

    private final Map<String, Class<?>> loadedClasses = new HashMap<>();

    public JelikClassLoader() {
        this("");
    }

    public JelikClassLoader(String jelikHome) {
        super(new URL[] { getDefaultPath() });

        try {
            var f = new File("./lib/bin");
            addURL(f.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(jelikHome)) {
            return;
        }

        try {
            var f = new File(jelikHome + "/api/jelik-lib/".replace("/", File.separator));
            addURL(f.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static URL getDefaultPath() {
        try {
            return new URL("file:/../jelik-compiler/target/classes/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUrl(URL url) {
        addURL(url);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    public Class<?> defineClass(String name, byte[] b) {
        log.info("Defining clas: " + name);

        var result = getLoadedClass(name);

        try {
            if (result == null) {
                result = defineClass(name, b, NumberUtils.INTEGER_ZERO, b.length);
            }
        } catch (Error e) {
            System.err.println(name);
            throw e;
        }

        loadedClasses.put(name, result);
        return result;
    }

    public Map<String, Class<?>> getLoadedClasses() {
        return loadedClasses;
    }

    public Class<?> getLoadedClass(String name) {
        return findLoadedClass(name);
    }

    public Optional<Class<?>> getLoadedClassOpt(String name) {
        return Optional.ofNullable(findLoadedClass(name));
    }

}
