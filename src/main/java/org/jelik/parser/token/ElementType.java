package org.jelik.parser.token;

/**
 * Enum for Token type in {@link org.jelik.parser.Lexer}
 *
 * @author Marcin Bukowiecki
 */
public enum ElementType {

    constructorKeyword,

    classKeyword,

    eof,

    throwKeyword,

    tryKeyword,

    catchKeyword,

    finallyKeyword,

    funKeyword,

    literal,

    questionMark,

    leftParenthesis,

    rightParenthesis,

    leftCurl,

    rightCurl,

    returnKeyword,


    comma,

    arrow,

    dot,

    add,

    newLine,

    packageKeyword,
    importKeyword,

    whitespace,

    colon,

    divide,
    modulo,
    mul,
    sub,

    range,

    inOperator,
    elvisOperator,

    mapCreate,
    maybeExpr,
    arrayCreate,

    rem,
    pow,
    assign,
    asOperator,

    isOperator,
    isNotOperator,
    referenceOperator,
    safeNavigation,
    combineOperator,
    incrOperator,
    prependOperator,
    appendOperator,

    orOperator,
    andOperator,

    bitwiseAnd,
    bitwiseXor,
    bitwiseOr,

    equalOperator,
    notEqualOperator,
    notOperator,
    decrOperator,

    shiftLeft,

    signedShiftRight,
    unsignedShiftRight,

    greaterOperator,
    greaterOrEqualOperator,

    lesserOperator,
    lesserOrEqualOperator,

    valKeyword,
    varKeyword,

    ifKeyword,
    elseKeyword,
    elifKeyword,
    thenKeyword,
    endKeyword,

    apostrophe,

    trueLiteral,
    falseLiteral,
    nullLiteral,

    leftBracket,
    rightBracket,
    staticKeyword, empty,
}
