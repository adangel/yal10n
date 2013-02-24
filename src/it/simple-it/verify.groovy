String buildlog = new File ( basedir, "build.log" ).getText("UTF-8");


assert buildlog.contains( "Updating ./svnrepos/repo1/trunk" )
assert buildlog.contains( "Updating ./svnrepos/repo1/a/trunk" )
assert buildlog.contains( "Updating ./svnrepos/repo2/trunk" )
assert buildlog.contains( "At revision 2" )
assert buildlog.contains( "At revision 1" )

assert buildlog.contains( "found 4 bundles:" )
assert buildlog.contains( "simple-it/target/checkouts/c7937f20686553c319293213284e171a/project-a/src/main/resources/ValidationMessages" )
assert buildlog.contains( "simple-it/target/checkouts/1afc426fea49bc354c5d54b55abc52cc/project-c/resources/messages" )
assert buildlog.contains( "simple-it/target/checkouts/c7937f20686553c319293213284e171a/project-a/src/main/resources/messages" )
assert buildlog.contains( "simple-it/target/checkouts/de637f03e130cf552a5b21f5666f2baa/project-b/src/main/resources/messages" )
