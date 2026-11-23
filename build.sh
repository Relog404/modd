#!/usr/bin/env sh
set -eu

if ! command -v java >/dev/null 2>&1; then
  echo "JDK 21 is required. Install a JDK, then run this script again." >&2
  exit 1
fi

java_version=$(java -version 2>&1 | sed -n '1s/.*version "\([0-9]*\).*/\1/p')
if [ -z "$java_version" ] || [ "$java_version" -lt 21 ]; then
  echo "JDK 21 or newer is required (detected Java ${java_version:-unknown})." >&2
  exit 1
fi

exec "$(dirname "$0")/gradlew" --no-daemon build "$@"
