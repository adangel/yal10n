String buildlog = new File ( basedir, "build.log" ).getText("UTF-8");


assert buildlog.contains( "Updating ./svnrepos/repo1/trunk" )
assert buildlog.contains( "Updating ./svnrepos/repo1/a/trunk" )
assert buildlog.contains( "Updating ./svnrepos/repo2/trunk" )
assert buildlog.contains( "At revision 2" )
assert buildlog.contains( "At revision 1" )

assert buildlog.contains( "found 4 bundles:" )
assert buildlog.contains( "simple-it/target/checkouts/c9c9f08f6bd09e476f02e52c82d02c3f/project-a/src/main/resources/ValidationMessages" )
assert buildlog.contains( "simple-it/target/checkouts/973dab5ccd92e4148131418d51fb5660/project-c/resources/messages" )
assert buildlog.contains( "simple-it/target/checkouts/0d1a4de8a865d2c4b1b562058f57ee3a/project-b/src/main/resources/messages" )
assert buildlog.contains( "simple-it/target/checkouts/c9c9f08f6bd09e476f02e52c82d02c3f/project-a/src/main/resources/messages" )
