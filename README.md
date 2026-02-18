# Lumex Language — Lexical Analyzer
**CS4031 Compiler Construction | Assignment 01 | Spring 2026**

---

## Team Members

| Name | Roll Number | Section |
|------|-------------|---------|
| Hadi Saleemi | 22i-1043 | E |
| Zainab Fatima | 22i-1064 | E |

---

## Language Overview

**Language Name:** Lumex (Luminous Expressions)
**File Extension:** `.lang`
**Paradigm:** Imperative / Procedural
**Case Sensitivity:** Yes — keywords are all lowercase; identifiers must start with uppercase

---

## Keywords

| Keyword | Meaning |
|---------|---------|
| `start` | Marks the beginning of a program block |
| `finish` | Marks the end of a program block |
| `loop` | Begins a loop construct |
| `condition` | Begins a conditional (if) block |
| `else` | Alternative branch of a conditional |
| `declare` | Variable declaration |
| `output` | Print/display output to console |
| `input` | Read input from user |
| `function` | Declares a function |
| `return` | Returns a value from a function |
| `break` | Exits a loop |
| `continue` | Skips to the next loop iteration |

---

## Identifier Rules

- Must **start with an uppercase letter** (A–Z)
- Followed by any combination of lowercase letters (a–z), digits (0–9), or underscores (`_`)
- Maximum length: **31 characters**
- Regex: `[A-Z][a-z0-9_]{0,30}`

### Examples

| Identifier | Valid? |
|------------|--------|
| `Count` | Valid |
| `Variable_name` | Valid |
| `X` | Valid |
| `Total_sum_2024` | Valid |
| `count` | Starts with lowercase |
| `2Count` | Starts with digit |
| `myVariable` | Starts with lowercase |

---

## Literal Formats

### Integer Literals
- Optional leading `+` or `-`, followed by one or more digits
- Regex: `[+-]?[0-9]+`
- Examples: `42`, `+100`, `-567`, `0`

### Floating-Point Literals
- Optional sign, digits, a decimal point, 1–6 decimal digits, optional exponent
- Regex: `[+-]?[0-9]+\.[0-9]{1,6}([eE][+-]?[0-9]+)?`
- Examples: `3.14`, `+2.5`, `-0.123456`, `1.5e10`, `2.0E-3`

### String Literals
- Enclosed in double quotes `" "`
- Supports escape sequences: `\"`, `\\`, `\n`, `\t`, `\r`
- Regex: `"([^"\\\n]|\\["\\ntr])*"`
- Example: `"Hello, World!"`, `"Line1\nLine2"`

### Character Literals
- Enclosed in single quotes `' '`
- Supports escape sequences: `\'`, `\\`, `\n`, `\t`, `\r`
- Regex: `'([^'\\\n]|\\['\\ntr])'`
- Examples: `'A'`, `'\n'`, `'\\'`

### Boolean Literals
- Exactly `true` or `false` (case-sensitive)

---

## Comment Syntax

### Single-Line Comments
Begin with `##` and continue to end of line.
```
## This is a single-line comment
```

### Multi-Line Comments
Begin with `#*` and end with `*#`.
```
#*
  This is a
  multi-line comment
*#
```

---

## Sample Programs

### Program 1 — Hello World
```
start
    output "Hello, World!";
finish
```

### Program 2 — Factorial Function
```
function Factorial
    declare Num = 5;
    declare Result = 1;
    loop Num > 0
        Result *= Num;
        Num--;
    finish
    output Result;
finish

start
    Factorial;
finish
```

### Program 3 — Even or Odd Checker
```
## Check if a number is even or odd
start
    declare Value = 0;
    input Value;
    condition Value % 2 == 0
        output "Even";
    else
        output "Odd";
    finish
finish
```
---

*Lumex — Luminous Expressions*