package graph_builder;

import java.util.ArrayList;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import config.IConfigurer;

public abstract class GraphBuilder {
	Graph graph;
	IConfigurer config;
	SVNRepository svnRepository;
	
	public GraphBuilder()
	{
		graph = new Graph();
		this.config = null;
		
		//setupConfiguration(config);
	}
	
	public void setConfigurer(IConfigurer config)
	{
		this.config = config;
		setupConfiguration(config);
	}
	
	public GraphBuilder(IConfigurer config)
	{
		graph = new Graph();
		this.config = config;
		
		setupConfiguration(config);
	}
	
	private void setupConfiguration(IConfigurer config){
		String url = config.getURL();
        String name = config.getUserName();
        String password = config.getPassword();
        
        setupLibrary();
        try {
        	svnRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            System.err.println("error while creating an SVNRepository for the location '" + url + "': " + svne.getMessage());
            System.exit(1);
        }

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        svnRepository.setAuthenticationManager(authManager);
	}
	
	protected abstract ArrayList<ChangedItem> extractItem(int revisionNumber);
	
	
	public void makeGraph(String outputFileName)
	{
		int revisionFrom = config.getStartRevision();
		int revisionTo = config.getEndRevision();
		// the graph creation code that uses extractItem.
		for(int i = revisionFrom; i <= revisionTo; i++){
			ArrayList<ChangedItem> changedItems =  extractItem(i);
			for(Iterator<ChangedItem> it=changedItems.iterator(); it.hasNext();)
			{
				ChangedItem item= it.next();
				if(item.action == 'A' || item.action == 'R'){
                	if(item.copyFromPath != null){
                		graph.changeKey(item.copyFromPath, item.name);                		
                		it.remove();
                	}
                }
			}
			
			for(Iterator<ChangedItem> it=changedItems.iterator(); it.hasNext();)
			{
				ChangedItem item= it.next();
				if(item.action == 'D'){
	                	graph.deleteNode(item.name);	                	
	                	it.remove();
	                }			
            }
			
			for(Iterator<ChangedItem> it=changedItems.iterator(); it.hasNext();)
			{
				ChangedItem item1= it.next();
        		for(ChangedItem item2 : changedItems){
                    graph.incrementEdge(item1.name, item2 .name, 1);
        		}
        	}			
        	graph.save(outputFileName);
		}
	}
	
	
	private static void setupLibrary() {
        //For using over http:// and https://
        DAVRepositoryFactory.setup();

        // For using over svn:// and svn+xxx:
        SVNRepositoryFactoryImpl.setup();
        
        //For using over file:///
        FSRepositoryFactory.setup();
    }

	public SVNRepository getSvnRepository() {
		return svnRepository;
	}

	public void setSvnRepository(SVNRepository svnRepository) {
		this.svnRepository = svnRepository;
	}
}
