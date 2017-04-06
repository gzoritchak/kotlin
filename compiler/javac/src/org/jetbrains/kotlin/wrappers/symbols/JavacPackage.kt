/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.wrappers.symbols

import org.jetbrains.kotlin.javac.Javac
import org.jetbrains.kotlin.load.java.structure.JavaPackage
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import javax.lang.model.element.PackageElement

class JavacPackage(val element: PackageElement, val javac: Javac) : JavaPackage {

    override val fqName = FqName(element.qualifiedName.toString())

    override val subPackages by lazy {
        javac.findSubPackages(element.qualifiedName.toString().let(::FqName))
    }

    override fun getClasses(nameFilter: (Name) -> Boolean) = javac.findClassesFromPackage(fqName)
            .filter { Name.isValidIdentifier(it.name.toString())
                      && nameFilter(Name.identifier(it.name.toString()))
            }

    override fun hashCode() = element.hashCode()

    override fun equals(other: Any?) = (other as? JavacPackage)?.element == element

    override fun toString() = element.qualifiedName.toString()

}