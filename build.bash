# this script is called when the judge is building your compiler.
# no argument will be passed in.

set -e
cd "$(dirname "$0")"
mkdir -p bin
find ./Compiler/src -name *.java | javac -d bin -classpath "lib/antlr-4.7.1-complete.jar" @/dev/stdin