#!/usr/bin/env bash
if [ $# -ne 2 ]; then
  echo "Usage: $0 new-version new-snapshot-version"
  exit 1
fi
new-version="$1"
new-snapshot-version="$2"
tmpOut=$(mktemp /tmp/mvn-release-tmp-out.XXXXXX)
mvn verify > "$tmpOut" 2>&1
if [ $? -eq 0 ]; then
  mvn versions:set -DnewVersion="${new-version}" > "$tmpOut" 2>&1 && \
  mvn versions:commit >> "$tmpOut" 2>&1 && \
  git add . >> "$tmpOut" 2>&1 && \
  git commit -m "preparing release ${new-version}" >> "$tmpOut" 2>&1 && \
  git tag -a "${new-version}" -m "release ${new-version}" >> "$tmpOut" 2>&1 && \
  mvn versions:set -DnewVersion="${new-snapshot-version}" >> "$tmpOut" 2>&1 && \
  mvn versions:commit >> "$tmpOut" 2>&1 && \
  git add . >> "$tmpOut" 2>&1 && \
  git commit -m "preparing next development iteration" >> "$tmpOut" 2>&1
  if [ $? -ne 0 ]; then
    echo "Problem with release: Nothing has been pushed yet! Please consult the logs and resolve the issues before pushing!"
    cat "$tmpOut"
  else
    echo "Successfully authored release ${new-version}"
    git push origin "${new-version}"
    git push
  fi
else
  echo "Error in mvn verify, please see attached log!"
  cat "$tmpOut"
fi
rm "$tmpOut"