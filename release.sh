#!/usr/bin/env bash
if [ $# -ne 2 ]; then
  echo "Usage: $0 new_version new_snapshot_version"
  exit 1
fi
new_version="$1"
new_snapshot_version="$2"
tmpOut=$(mktemp /tmp/mvn-release-tmp-out.XXXXXX)
mvn verify > "$tmpOut" 2>&1
if [ $? -eq 0 ]; then
  mvn versions:set -DnewVersion="${new_version}" > "$tmpOut" 2>&1 && \
  mvn versions:commit >> "$tmpOut" 2>&1 && \
  git add . >> "$tmpOut" 2>&1 && \
  git commit -m "preparing release ${new_version}" >> "$tmpOut" 2>&1 && \
  git tag -a "${new_version}" -m "release ${new_version}" >> "$tmpOut" 2>&1 && \
  mvn versions:set -DnewVersion="${new_snapshot_version}" >> "$tmpOut" 2>&1 && \
  mvn versions:commit >> "$tmpOut" 2>&1 && \
  git add . >> "$tmpOut" 2>&1 && \
  git commit -m "preparing next development iteration" >> "$tmpOut" 2>&1
  if [ $? -ne 0 ]; then
    echo "Problem with release: Nothing has been pushed yet! Please consult the logs and resolve the issues before pushing!"
    cat "$tmpOut"
  else
    echo "Successfully authored release ${new_version}"
    git push origin "${new_version}"
    git push
  fi
else
  echo "Error in mvn verify, please see attached log!"
  cat "$tmpOut"
fi
rm "$tmpOut"