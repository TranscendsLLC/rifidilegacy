<map version="0.7.1">
<node TEXT="Edge Client Eclipse Commands">
<node TEXT="connect" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.edgeserver.connect"/>
<node TEXT="Purpose: Connect to a Remote Edge Server"/>
</node>
<node TEXT="Refresh" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.edgeserver.update"/>
<node TEXT="Purpose: Refresh Model of Edge Server"/>
</node>
<node TEXT="Disconnect" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.edgeserver.disconnect"/>
<node TEXT="Purpose: Disconnect from Edge Server"/>
</node>
<node TEXT="Save" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.edgeserver.save"/>
<node TEXT="Purpose: Save an Edge Server Configuration (on the server)"/>
</node>
<node TEXT="create reader" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.edgeserver.newreader"/>
<node TEXT="Purpose: Create a new Reader"/>
</node>
<node TEXT="delete reader" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.reader.delete"/>
<node TEXT="Purpose: Delete a Reader"/>
</node>
<node TEXT="Create Session" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.reader.createsession"/>
<node TEXT="Purpose: Create a session on a reader"/>
</node>
<node TEXT="Delete Session" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.session.delete"/>
<node TEXT="Purpose: Delete a session from a Reader"/>
</node>
<node TEXT="Start Session" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.session.start"/>
<node TEXT="Purpose: Move the session to the processing state"/>
</node>
<node TEXT="Stop Session" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.session.stop"/>
<node TEXT="Purpose: Move session to Closed state"/>
</node>
<node TEXT="Create Command Configuration" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.commandconfig.create"/>
<node TEXT="Purpose: Create a new command configuration"/>
</node>
<node TEXT="Delete Command Configuration" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.commandconfig.delete"/>
<node TEXT="Purpose: Delete a command configuration"/>
</node>
<node TEXT="Kill Job" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.jobs.kill"/>
<node TEXT="Purpose: Stop a command from executing on a session"/>
</node>
<node TEXT="Submit Job" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.jobs.submit"/>
<node TEXT="Purpose: submit a command to a session for execution"/>
</node>
<node TEXT="Clear Reader Properties" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.reader.clearpropertychanges"/>
<node TEXT="Reset changed reader properties to original values"/>
</node>
<node TEXT="Sync Reader Properties" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.reader.synchpropertychanges"/>
<node TEXT="Commit changed reader properties to reader configuration"/>
</node>
<node TEXT="Clear Command Properties" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.commandconfig.clearpropertychanges"/>
<node TEXT="Reset changed command configuration properties to original values"/>
</node>
<node TEXT="Sync Command Properties" POSITION="right">
<node TEXT="org.rifidi.edge.client.sal.commands.commandconfig.synchpropertychanges"/>
<node TEXT="Commit changed command configuration properties to command configuraiton on server"/>
</node>
</node>
</map>
