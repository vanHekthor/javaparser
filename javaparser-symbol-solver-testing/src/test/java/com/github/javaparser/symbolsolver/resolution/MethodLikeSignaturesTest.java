/*
 * Copyright (C) 2015-2016 Federico Tomassetti
 * Copyright (C) 2017-2019 The JavaParser Team.
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

package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.resolution.Navigator;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodLikeSignaturesTest extends AbstractResolutionTest {

    @Test
    void checkReflectionConstructorSignature() {
        CompilationUnit cu = parseSample("MethodLikeSignaturesTest");
        ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "MethodLikeSignaturesTest");
        MethodDeclaration method = Navigator.demandMethod(clazz, "foo");
        ObjectCreationExpr objectCreationExpr = method.getBody().get().getStatements().get(0)
                                                        .asExpressionStmt().getExpression().asVariableDeclarationExpr()
                                                        .getVariable(0).getInitializer().get().asObjectCreationExpr();

        ResolvedConstructorDeclaration resolvedConstructorDeclaration =
                JavaParserFacade.get(new ReflectionTypeSolver()).solve(objectCreationExpr).getCorrespondingDeclaration();

        assertEquals("File", resolvedConstructorDeclaration.getName());
        assertEquals("File(java.lang.String)", resolvedConstructorDeclaration.getSignature());
        assertEquals("java.io.File.File(java.lang.String)", resolvedConstructorDeclaration.getQualifiedSignature());
    }

    @Test
    void checkReflectionMethodSignature() {
        CompilationUnit cu = parseSample("MethodLikeSignaturesTest");
        ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "MethodLikeSignaturesTest");
        MethodDeclaration method = Navigator.demandMethod(clazz, "foo");
        MethodCallExpr methodCallExpr = method.getBody().get().getStatements().get(1)
                                                .asExpressionStmt().getExpression().asMethodCallExpr();

        ResolvedMethodDeclaration resolvedMethodDeclaration =
                JavaParserFacade.get(new ReflectionTypeSolver()).solve(methodCallExpr).getCorrespondingDeclaration();

        assertEquals("delete", resolvedMethodDeclaration.getName());
        assertEquals("delete()", resolvedMethodDeclaration.getSignature());
        assertEquals("java.io.File.delete()", resolvedMethodDeclaration.getQualifiedSignature());
    }
}
