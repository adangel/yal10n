String buildlog = new File ( basedir, "build.log" ).getText( "UTF-8" )

assert buildlog.contains( "Updating ./svnrepos/repo1/trunk" )
assert buildlog.contains( "At revision 2" )

assert buildlog.contains( "Found 1 bundles:" )

assert buildlog.contains( "SkipEmail is true. Do not send the following message:" )
assert buildlog.contains( "From: andreas@localhost; \n"
                        + "To: andreas@localhost; andreas@localhost; \n"
                        + "Subject: [yal10n] changes in Unknown Project 0\n" )
assert buildlog.contains( 'See here: <a href="http://svn/viewvc/repo1/trunk/messages.properties?r1=1&r2=2">http://svn/viewvc/repo1/trunk/messages.properties?r1=1&r2=2</a>' )
assert buildlog.contains( "messages.properties\t(revision 1)" )
assert buildlog.contains( "messages.properties\t(revision 2)" )
assert buildlog.contains( "baz=f123" )
assert buildlog.contains( "baz=foo123" )
