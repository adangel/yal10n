String buildlog = new File ( basedir, "build.log" ).getText( "UTF-8" )

assert buildlog.contains( "Updating ./svnrepos/repo1/trunk" )
assert buildlog.contains( "Updating ./svnrepos/repo1/a/trunk" )
assert buildlog.contains( "Updating ./svnrepos/repo2/trunk" )
assert buildlog.contains( "At revision 4" )
assert buildlog.contains( "At revision 1" )

assert buildlog.contains( "Found 4 bundles:" )

// check sorting of the bundle processing
index1 = buildlog.indexOf( "simple-it/target/checkouts/0d1a4de8a865d2c4b1b562058f57ee3a/project-b/src/main/resources/messages" )
assert index1 > -1
index2 = buildlog.indexOf( "simple-it/target/checkouts/973dab5ccd92e4148131418d51fb5660/project-c/resources/messages" );
assert index2 > -1
index3 = buildlog.indexOf( "simple-it/target/checkouts/c9c9f08f6bd09e476f02e52c82d02c3f/project-a/src/main/resources/ValidationMessages" );
assert index3 > -1
index4 = buildlog.indexOf( "simple-it/target/checkouts/c9c9f08f6bd09e476f02e52c82d02c3f/project-a/src/main/resources/messages" );
assert index4 > -1

assert index1 < index2 && index2 < index3 && index3 < index4


File dashboard = new File ( basedir, "target/dashboard.html" )
assert dashboard.exists()

// check language sorting - variants at the end
String dashboardContent = dashboard.getText( "UTF-8" )
index1 = dashboardContent.indexOf( "de" )
assert index1 > -1
index2 = dashboardContent.indexOf( "fr" )
assert index2 > -1
index3 = dashboardContent.indexOf( "de_DE" )
assert index3 > -1

assert index1 < index2 && index2 < index3

// verify that default.css has been extracted to target
assert new File ( basedir, "target/default.css" ).exists()
// verify that webjars have been extracted
assert new File ( basedir, "target/webjars/foundation" ).exists()
assert new File ( basedir, "target/webjars/jquery" ).exists()
assert new File ( basedir, "target/webjars/modernizr" ).exists()
assert new File ( basedir, "target/webjars/normalize.css" ).exists()
