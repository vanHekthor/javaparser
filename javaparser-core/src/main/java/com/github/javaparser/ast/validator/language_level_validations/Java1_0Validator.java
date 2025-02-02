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
package com.github.javaparser.ast.validator.language_level_validations;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.validator.*;
import com.github.javaparser.ast.validator.language_level_validations.chunks.CommonValidators;
import com.github.javaparser.ast.validator.language_level_validations.chunks.ModifierValidator;
import com.github.javaparser.ast.validator.language_level_validations.chunks.NoBinaryIntegerLiteralsValidator;
import com.github.javaparser.ast.validator.language_level_validations.chunks.NoUnderscoresInIntegerLiteralsValidator;

/**
 * This validator validates according to Java 1.0 syntax rules.
 */
public class Java1_0Validator extends Validators {

    final Validator modifiersWithoutStrictfpAndDefaultAndStaticInterfaceMethodsAndPrivateInterfaceMethods = new ModifierValidator(false, false, false);

    final Validator noAssertKeyword = new SimpleValidator<>(AssertStmt.class, n -> true, (n, reporter) -> reporter.report(n, "'assert' keyword is not supported."));

    final Validator noInnerClasses = new SimpleValidator<>(ClassOrInterfaceDeclaration.class, n -> !n.isTopLevelType(), (n, reporter) -> reporter.report(n, "inner classes or interfaces are not supported."));

    final Validator noReflection = new SimpleValidator<>(ClassExpr.class, n -> true, (n, reporter) -> reporter.report(n, "Reflection is not supported."));

    final Validator noGenerics = new TreeVisitorValidator((node, reporter) -> {
        if (node instanceof NodeWithTypeArguments) {
            if (((NodeWithTypeArguments<? extends Node>) node).getTypeArguments().isPresent()) {
                reporter.report(node, "Generics are not supported.");
            }
        }
        if (node instanceof NodeWithTypeParameters) {
            if (((NodeWithTypeParameters<? extends Node>) node).getTypeParameters().isNonEmpty()) {
                reporter.report(node, "Generics are not supported.");
            }
        }
    });

    final SingleNodeTypeValidator<TryStmt> tryWithoutResources = new SingleNodeTypeValidator<>(TryStmt.class, (n, reporter) -> {
        if (n.getCatchClauses().isEmpty() && !n.getFinallyBlock().isPresent()) {
            reporter.report(n, "Try has no finally and no catch.");
        }
        if (n.getResources().isNonEmpty()) {
            reporter.report(n, "Catch with resource is not supported.");
        }
    });

    final Validator noAnnotations = new TreeVisitorValidator((node, reporter) -> {
        if (node instanceof AnnotationExpr || node instanceof AnnotationDeclaration) {
            reporter.report(node, "Annotations are not supported.");
        }
    });

    final Validator noEnums = new SimpleValidator<>(EnumDeclaration.class, n -> true, (n, reporter) -> reporter.report(n, "Enumerations are not supported."));

    final Validator noVarargs = new SimpleValidator<>(Parameter.class, Parameter::isVarArgs, (n, reporter) -> reporter.report(n, "Varargs are not supported."));

    final Validator noForEach = new SimpleValidator<>(ForEachStmt.class, n -> true, (n, reporter) -> reporter.report(n, "For-each loops are not supported."));

    final Validator noStaticImports = new SimpleValidator<>(ImportDeclaration.class, ImportDeclaration::isStatic, (n, reporter) -> reporter.report(n, "Static imports are not supported."));

    final Validator onlyOneLabelInSwitchCase = new SimpleValidator<>(SwitchEntry.class, n -> n.getLabels().size() > 1, (n, reporter) -> reporter.report(n.getLabels().getParentNode().get(), "Only one label allowed in a switch-case."));

    final Validator noYield = new SimpleValidator<>(YieldStmt.class, n -> true, (n, reporter) -> reporter.report(n, "Only labels allowed in break statements."));

    final Validator noBinaryIntegerLiterals = new NoBinaryIntegerLiteralsValidator();

    final Validator noUnderscoresInIntegerLiterals = new NoUnderscoresInIntegerLiteralsValidator();

    final Validator noMultiCatch = new SimpleValidator<>(UnionType.class, n -> true, (n, reporter) -> reporter.report(n, "Multi-catch is not supported."));

    final Validator noLambdas = new SimpleValidator<>(LambdaExpr.class, n -> true, (n, reporter) -> reporter.report(n, "Lambdas are not supported."));

    final Validator noModules = new SimpleValidator<>(ModuleDeclaration.class, n -> true, (n, reporter) -> reporter.report(n, "Modules are not supported."));

    final Validator noSwitchExpressions = new SimpleValidator<>(SwitchExpr.class, n -> true, (n, reporter) -> reporter.report(n, "Switch expressions are not supported."));

    final Validator noPatternMatchingInstanceOf = new SimpleValidator<>(InstanceOfExpr.class, n -> n.getPattern().isPresent(), (n, reporter) -> reporter.report(n, "Use of patterns with instanceof is not supported."));

    final Validator noTextBlockLiteral = new SimpleValidator<>(TextBlockLiteralExpr.class, n -> true, (n, reporter) -> reporter.report(n, "Text Block Literals are not supported."));

    final Validator noRecordDeclaration = new SimpleValidator<>(RecordDeclaration.class, n -> true, (n, reporter) -> reporter.report(n, "Record Declarations are not supported."));

    public Java1_0Validator() {
        super(new CommonValidators());
        add(modifiersWithoutStrictfpAndDefaultAndStaticInterfaceMethodsAndPrivateInterfaceMethods);
        add(noAssertKeyword);
        add(noInnerClasses);
        add(noReflection);
        add(noGenerics);
        add(tryWithoutResources);
        add(noAnnotations);
        add(noEnums);
        add(noVarargs);
        add(noForEach);
        add(noStaticImports);
        add(noYield);
        add(onlyOneLabelInSwitchCase);
        add(noBinaryIntegerLiterals);
        add(noUnderscoresInIntegerLiterals);
        add(noMultiCatch);
        add(noLambdas);
        add(noModules);
        add(noSwitchExpressions);
        add(noPatternMatchingInstanceOf);
        add(noTextBlockLiteral);
        add(noRecordDeclaration);
    }
}
