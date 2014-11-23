import java.util.Arrays;
import java.util.HashSet;

                + "@@ -1,8 +1,8 @@\n"
                + " eight - no change\n"
                + " nine - no change\n"
                + "@@ -16,5 +16,4 @@\n"
                + " nine 8 - no change\n"
                + " nine 9 - no change\n"
                + " ten - no change\n"
                + "-eleven - removed\n"
                + " twelve - no change\n";
        Assert.assertTrue( html.contains( "Line 16" ) );
        Assert.assertTrue( asString.contains( "-19  eleven - removed" ) );

    /**
     * Tests that a block of changes is detected correctly.
     */
    @Test
    public void testDiffWithMultipleChangedLinesInBlock()
    {
        String diff =
                  "Index: trunk/messages.properties\n"
                + "===================================================================\n"
                + "--- trunk/messages.properties (1)\n"
                + "+++ trunk/messages.properties (2)\n"
                + "@@ -320,10 +321,10 @@\n"
                + " save-dialog.header=Save\n"
                + " save-dialog.button.ok=Ok\n"
                + " save-dialog.button.cancel=Cancel\n"
                + "-save-dialog.status.New=New\n"
                + "-save-dialog.status.Completed=Completed\n"
                + "-save-dialog.status.In-process=In Process\n"
                + "-save-dialog.status.In-review=In Review\n"
                + "+save-dialog.status.NEW=New\n"
                + "+save-dialog.status.COMPLETED=Completed\n"
                + "+save-dialog.status.IN_PROCESS=In Process\n"
                + "+save-dialog.status.IN_REVIEW=In Review\n"
                + " save-dialog.footer.text1=Footer Text 1\n"
                + " save-dialog.footer.text2=Footer Text 2\n";
        UnifiedDiff unifiedDiff = new UnifiedDiff( diff );
        Assert.assertEquals( 1, unifiedDiff.getHunks().size() );
        Hunk hunk = unifiedDiff.getHunks().get( 0 );
        Assert.assertEquals( 5, hunk.commonLines.size() );
        Assert.assertEquals( hunk.origLines.keySet(), hunk.newLines.keySet() );
        final HashSet<Integer> expectedChangedLines = new HashSet<Integer>( Arrays.asList( 323, 324, 325, 326 ) );
        Assert.assertEquals( expectedChangedLines, hunk.origLines.keySet() );
    }