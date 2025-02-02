/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2021 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
package com.github.javaparser.resolution;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

public interface SymbolResolver {

    /**
     * For a reference it would find the corresponding declaration.
     */
    <T> T resolveDeclaration(Node node, Class<T> resultClass);

    /**
     * For types it would find the corresponding resolved types.
     */
    <T> T toResolvedType(Type javaparserType, Class<T> resultClass);

    /**
     * For an expression it would find the corresponding resolved type.
     */
    ResolvedType calculateType(Expression expression);
    
    /**
     * For a node it would find the corresponding reference type declaration.
     */
    ResolvedReferenceTypeDeclaration toTypeDeclaration(Node node);
}
