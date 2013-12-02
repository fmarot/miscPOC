/** Dir where are stored your miscelaneous git repo */
origDir = new File('./orig')
/** Source directory for the script, akka working directory. WARNING: will be MODIFIED */
sourceDir = new File('./source')
/** Directory where is sotred the single merged git repo */
targetDir = new File('./target')
/** Path to the app found here: git@github.com:robinst/git-merge-repos.git . It's author "Robin Stocker" is a living god ! */
migrationGitHubAppPath="/home/francois/TMP/git-merge-repos/run.sh"


// Will generate an executable script
outputUnixScript = new File("gitMergeRepos.sh")
String scriptAsString = getHeader()
origDir.eachDir{ dir ->
	scriptAsString += getContentForDir(dir)
}
scriptAsString += getFooter()
// println "-----------------" + scriptAsString
outputUnixScript.text = scriptAsString
// Set output script as executable
builder = new AntBuilder()
builder.chmod(file:outputUnixScript, perm:'+x')
println "scrit generated..."

String getHeader () {
return """#!/bin/bash -vx
# From https://github.com/robinst/git-merge-repos

echo "Script header copy everything from '${origDir.name}' dir to '${sourceDir.name}' dir."
echo "'${origDir.name}' is to be left unmodified. WARNING: '${sourceDir.name}' directory will be modified"

rm -r -f ${sourceDir.name}
cp -r ${origDir.name} ${sourceDir.name}
rm -r -f ${targetDir.name}
mkdir ${targetDir.name}

mergeArgs=""

cd ${sourceDir.canonicalPath}
"""
}


String getFooter () {
	return """
echo "======================= Change history done ========================"

cd ${targetDir.canonicalPath}
echo "will run ${migrationGitHubAppPath} \${mergeArgs}"
${migrationGitHubAppPath} \${mergeArgs}

echo "WARNING: please lookout for this message which means failure: Cannot rewrite branches: You have unstaged changes."

"""
}


String getContentForDir(File dir) {
return """
echo "======================= Will rewrite history for ${dir.name} (push in subdirectory) ========================"
cd ${dir.name}
mergeArgs="\$mergeArgs ${dir.canonicalPath}/.git/:."

cd ${dir.canonicalPath}
# Checkout all branches
for remote in `git branch -r | grep -v master `; do git checkout --track \$remote ; done
git remote rm origin
git checkout master
git filter-branch --index-filter \
  'git ls-files -s | sed "s-\\t\\"*-&${dir.name}/-" | GIT_INDEX_FILE=\$GIT_INDEX_FILE.new git update-index --index-info && mv "\$GIT_INDEX_FILE.new" "\$GIT_INDEX_FILE"' \\
  --tag-name-filter cat \\
  -- --all

cd ..

"""
}



