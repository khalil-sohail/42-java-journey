#!/usr/bin/env bash

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}==================================================${NC}"
echo -e "${BLUE}           42 JAVA PISCINE - EX03 TESTER          ${NC}"
echo -e "${BLUE}==================================================${NC}"

if [ ! -f "Program.java" ]; then
    echo -e "${RED}[ERROR] Program.java not found in the current directory.${NC}"
    echo -e "Make sure you run this script from the folder containing your ex03 source code."
    exit 1
fi

rm -f *.class

echo -e "${YELLOW}Compiling Program.java...${NC}"
javac Program.java
if [ $? -ne 0 ]; then
    echo -e "${RED}[FAIL] Compilation failed!${NC}"
    exit 1
fi
echo -e "${GREEN}[SUCCESS] Compiled successfully.${NC}\n"

TOTAL_TESTS=0
PASSED_TESTS=0

run_test() {
    local test_name="$1"
    local input_data="$2"
    local expected_exit="$3"
    local expected_content="$4"

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -e "${BLUE}Test #$TOTAL_TESTS: $test_name${NC}"
    echo -e "--- Input ---"
    echo -e "$input_data"
    echo -e "-------------"

    local output
    output=$(echo -e "$input_data" | java Program 2>&1)
    local exit_code=$?

    local exit_pass=0
    if [ "$exit_code" -eq "$expected_exit" ]; then
        exit_pass=1
    fi

    local content_pass=0
    if echo "$output" | grep -qE "$expected_content"; then
        content_pass=1
    fi

    if [ "$exit_pass" -eq 1 ] && [ "$content_pass" -eq 1 ]; then
        echo -e "${GREEN}[PASS] Exit code: $exit_code (Expected: $expected_exit)${NC}"
        echo -e "${GREEN}[PASS] Output contains expected patterns.${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}[FAIL] Test Failed!${NC}"
        echo -e "Expected Exit Code: $expected_exit, Got: $exit_code"
        echo -e "Expected Pattern:   \"$expected_content\""
        echo -e "Actual Output:"
        echo -e "${YELLOW}$output${NC}"
    fi
    echo -e "==================================================\n"
}


run_test "Subject Example (Valid Chronological)" \
"Week 1
4 5 2 4 2
Week 2
7 7 7 7 6
Week 3
4 3 4 9 8
Week 4
9 9 4 6 7
42" \
0 \
"Week 1 ==|Week 2 ======|Week 3 ===|Week 4 ===="

run_test "Duplicate Weeks" \
"Week 1
4 5 2 4 2
Week 1
7 7 7 7 6
42" \
255 \
"IllegalArgument"

run_test "Out of Order Weeks (Week 2 before Week 1)" \
"Week 2
7 7 7 7 6
Week 1
4 5 2 4 2
42" \
255 \
"IllegalArgument"

run_test "Out of Bounds Week Number" \
"Week 19
4 5 2 4 2
42" \
255 \
"IllegalArgument"

run_test "Non-numeric Grades" \
"Week 1
4 5 A 4 2
42" \
255 \
"IllegalArgument"

run_test "Out of Range Grade (> 9)" \
"Week 1
4 5 10 4 2
42" \
255 \
"IllegalArgument"

run_test "Missing Grades (Only 4 entered)" \
"Week 1
4 5 2 4
Week 2
7 7 7 7 6
42" \
255 \
"IllegalArgument"

run_test "Excess Grades (6 entered)" \
"Week 1
4 5 2 4 2 9
Week 2
7 7 7 7 6
42" \
255 \
"IllegalArgument"

run_test "Wrong Header Keyword" \
"Day 1
4 5 2 4 2
42" \
255 \
"IllegalArgument"

echo -e "${BLUE}--- TEST SUMMARY ---${NC}"
if [ "$PASSED_TESTS" -eq "$TOTAL_TESTS" ]; then
    echo -e "${GREEN}ALL $PASSED_TESTS / $TOTAL_TESTS TESTS PASSED! 🎉${NC}"
else
    echo -e "${RED}SOME TESTS FAILED! ($PASSED_TESTS / $TOTAL_TESTS passed)${NC}"
    echo -e "Review the outputs above to debug your program."
fi
echo -e "${BLUE}--------------------${NC}"

rm -f *.class
