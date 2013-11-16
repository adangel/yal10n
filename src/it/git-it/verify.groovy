String buildlog = new File ( basedir, "build.log" ).getText( "UTF-8" )

assert buildlog.contains( "Updating ./gitrepos/repo1/.git" )
assert buildlog.contains( "At revision f5d50077a92f9e29d704518ab2fbd9ecf7307214" )

assert buildlog.contains( "Found 1 bundles:" )

index1 = buildlog.indexOf( "git-it/target/checkouts/e79b88d8c3e5497a3c65deeefedc3e95/project-a/src/main/resources/messages" )
assert index1 > -1

File dashboard = new File ( basedir, "target/dashboard.html" )
assert dashboard.exists()
