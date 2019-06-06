package mycode;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class DepInstall {

	public final Integer MAXCOMS = 1000;
	

	public DepInstall() {
		// TODO
		
	}
	
	
	public void runNCommands (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands
		
		// TODO
		
		Vector<String> installed = new Vector<String>();
		
		int i = 0;
		int LIMIT;
		if(commands.size() < N) 
			LIMIT = commands.size();
		else
			LIMIT = N;
		
		// First, let's count how many DEPEND lines we're dealing with
		int p = 0;
		int depend_count = 0;
		while(p < LIMIT) {
			if(commands.get(i).contains("DEPEND")) {
				depend_count++;
			}
			p++;
		}
		
		
		// PLAN:
		// We'll create a vector<String> of vector<String>'s and that's DEPEND done.
		// Then for INSTALL, we only need to check the first element of each vector<String>

		
		if(depend_count > LIMIT) {
			// then we need smaller Array[][]   (but let's use Vector<String> instead of Array[][] somehow?)
			depend_count = LIMIT;
		}	
		Vector<Vector<String>> dependees = new Vector<Vector<String>>(depend_count);
			// dependees will store every single DEPEND string's program names
		
		int X = 0; // this will be used to count each new line we insert
		
		while(i < commands.size()) { 
			// "   " case
			
			if(commands.get(i).contains("   "))
				i++;
			
			// List case
			if(commands.get(i).contains("LIST")) {
				int i_1 = installed.size() - 1;
				while(i_1 >= 0) {
					commands.add(i+1,"   "+installed.get(i_1));
					i_1--;
				}
			}

			
			// Depend case
			if(commands.get(i).contains("DEPEND")) {
				// Create a depend vector<string>?
				dependees.add(read(commands.get(i)));
			}
			
			// Install case
			if(commands.get(i).contains("INSTALL")) {

				if(installed.contains(read(commands.get(i)).get(0))) {	// If already installed, will inform
					commands.add(i+1, "   "+read(commands.get(i)).get(0)+" is already installed");
				}
				else { // Just installs everything that is relevant
					int u=0;
					int catch_u = -1;
					while(u < dependees.size()) { // finds 
						if(dependees.get(u).get(0).contains(read(commands.get(i)).get(0))) {
							catch_u = u;
						}
						u++;
					}
					if(catch_u == -1) { // Relies on nothing, so we just install it
						commands.add(i+1, "   Installing "+read(commands.get(i)).get(0));
						installed.add(read(commands.get(i)).get(0)); // Check this later
						X++;
					}
					else { // Relies on some things, so we install what's necessary
						Vector<String> temp = new Vector<String>();
						int e = 0;
						int e_reverse = dependees.get(catch_u).size()-1;
						while(e < dependees.get(catch_u).size()) { 
							if(!(installed.contains(dependees.get(catch_u).get(e)))) {
								commands.add(i+1, "   Installing "+dependees.get(catch_u).get(e));
								temp.add(dependees.get(catch_u).get(e));
								//System.out.println(installed.toString());
							}
							X++;
							e++;
							e_reverse--;
						} 
						Collections.reverse(temp);
						installed.addAll(temp);
					}
					
				}
				
				// if doesn't depend on anything, then "   Installing X"
				if(!(installed.contains(read(commands.get(i)).get(0)))) { // Works
					commands.add(i+1,"   Installing "+ read(commands.get(i)).get(0));
					installed.add(read(commands.get(i)).get(0));
				}
			}
			
			// Remove case
			if(commands.get(i).contains("REMOVE")) {
				// 0) If hasn't been installed, then "   X is not installed"	- done
				int index_0 = -1;
				
				
				if(!(installed.contains(read(commands.get(i)).get(0)))) {
					commands.add(i+1,"   "+read(commands.get(i)).get(0) +" is not installed");
				}
				
				else {

					int t_0 = 0, t_1;
					boolean relies = false;
					while(t_0 < dependees.size()) {
						t_1 = 1;
						while(t_1 < dependees.get(t_0).size()) {
							if(dependees.get(t_0).get(t_1).contains(read(commands.get(i)).get(0))) {
								relies = true;
							}
							t_1++;
						}
						t_0++;
					}
					
					
					t_0 = 0;
					while(t_0 < dependees.size()) {
						if(dependees.get(t_0).contains(read(commands.get(i)).get(0)))
							index_0 = t_0;
						t_0++;
					}
					
					
					//System.out.println("index_0 = "+index_0);
					if(relies == true) {
						commands.add(i+1,"   "+read(commands.get(i)).get(0) +" is still needed");
					}
					else { // Remove it and anything needed with it
						commands.add(i+1,"   Removing "+read(commands.get(i)).get(0));
						Collections.reverse(installed);
						installed.remove(read(commands.get(i)).get(0));
						Collections.reverse(installed);
						
						// Remove others if it depended on them, so long as nothing else relies on them!
						// i.e. 1) Check if read(commands.get(i)).get(0) depends on anything
						// then 2) Check if those are dependent on anything else
						// if so, then 3) Remove them too

						if(index_0 != -1) {
							
							int k00 = 1;
							Vector<String> temp = dependees.get(index_0);
							while(k00 < dependees.get(index_0).size()) {
								int i00 = 0;
								int j00;
								boolean relies_on_something_else = false;
								
								while(i00 < dependees.size()) {
									j00=1;
									if(i00 != index_0) {
										
										while(j00 < dependees.get(i00).size()) {
											//System.out.println("Dependees = "+dependees.toString());
											//System.out.println("Seeing if "+dependees.get(index_0).get(k00)+" = "+ dependees.get(i00).get(j00)+" where i00="+i00+", j00 ="+j00);
											if(dependees.get(index_0).get(k00).contains(dependees.get(i00).get(j00))) {
												relies_on_something_else = true;
											}
											j00++;
										}
										
									}
									i00++;
								}
								
								if(relies_on_something_else == false) {
									commands.add(i+2,"   Removing "+dependees.get(index_0).get(k00));
									Collections.reverse(installed);
									installed.remove(dependees.get(index_0).get(k00));
									Collections.reverse(installed);
								}
								
								k00++;
							}
							//temp.remove(0);
							//System.out.println(temp.toString());
							dependees.remove(index_0);
						}

					}
				}
				
				// 1) If nothing depends on it, then "   Removing X"			- done
				// 2) If anything depends on it, then "   X is still needed"	- done
				
			}
			//System.out.println(installed.toString());
			i++;
		}
		
		// Now we have 'all commands established', now we'll remove every duplicate containing "   " --- BAD
		// But first, going to take care of LIMIT problem first, otherwise this can become too complicated to fix
		int i_0 = 0, j_0;
		int X_0 = commands.size(); // So we can count X easily
		int counting = 0;

		X_0 = X_0 - commands.size();
		
		// limit = n = commands.size() - LIMIT ---> -n = LIMIT - commands.size() ---> want LIMIT
		

		int sorted_size = 0;
		for(int i1 = 0; i1 < commands.size(); i1++) { // worthless
			if(!(commands.get(i1).contains("   ")))
				sorted_size++;
		}
		
		// Counting how many new lines created from each command line
		int[] extra_lines = new int[sorted_size];
		
		int i0 = 0;
		int count = 0;
		while(i0 < commands.size()-1) { // 
			int j0 = i0+1;
			while(commands.get(j0).contains("   ")) {
				j0++;
				extra_lines[count]++;
			}
			//System.out.println("extra_lines["+count+"] = "+extra_lines[count]);
			i0 = j0;
			count++;
		}
		//System.out.println("sorted_size = "+sorted_size);

		// Now, using above, we see how many extra lines we have to add to limit
		// i.e. find Q
		int Q = 0;
		for(int q=0;q<LIMIT;q++) {
			Q = Q + extra_lines[q];
		}

		//X_0 = X - X_0;
		int pineapple = 0;
		while(pineapple < LIMIT + Q) { // must = min(N,size) + Q
			System.out.println(commands.get(pineapple)); // LIMIT stays constant it seems, good. So, we'll have LIMIT + X 
			pineapple++;
		}
	}
	
	
	// New method, collects every word from a string
	public Vector<String> read(String s) {
		String[] words = s.split("\\s+");
		for (int i = 0; i < words.length; i++) 
		    words[i] = words[i].replaceAll("[^\\w]", "");
		Vector<String> temp = new Vector<String>(Arrays.asList(words));
		temp.remove(0);
		return temp;
	}
	

	
	public void runNCommandswCheck (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands, checking for cycles
		
		// TODO
		
		Vector<String> installed = new Vector<String>();
		boolean installable = true;
		int i = 0;
		int LIMIT;
		if(commands.size() < N) 
			LIMIT = commands.size();
		else
			LIMIT = N;
		
		// First, let's count how many DEPEND lines we're dealing with
		int p = 0;
		int depend_count = 0;
		while(p < LIMIT) {
			if(commands.get(i).contains("DEPEND")) {
				depend_count++;
			}
			p++;
		}
		int commands_original_size = commands.size();
		

		
		
		
		if(depend_count > LIMIT) {
			depend_count = LIMIT;
		}	
		Vector<Vector<String>> dependees = new Vector<Vector<String>>(depend_count);
		
		int X = 0; // this will be used to count each new line we insert
		
		while(i < commands.size()) { 

			
			// Depend case
			if(commands.get(i).contains("DEPEND")) {
				// Create a depend vector<string>?
				dependees.add(read(commands.get(i)));
			}
			
			// Check for cycles first:
			Vector<String> dependants = new Vector<String>();
			dependants.addAll(dependees.firstElement());

			int i_0 = 0;
			int j_0;
			while(i_0 < dependees.size()) {
				j_0 = 0;
				while(j_0 < dependees.get(i_0).size()) {
					if(!(dependants.contains(dependees.get(i_0).get(j_0)))) {
						dependants.add(dependees.get(i_0).get(j_0));
					}
					j_0++;
				}
				i_0++;
			}
			

			
			//Graph graphy = new Graph();
			//for(int p1=0;p1<dependants.size();p1++) {
			//	graphy.addVertex(p1);
			//	graphy.getVertex(p1).addAdj(p1);
			//}
			Graph graphy = new Graph();
			Vector<Edge> edges = new Vector<Edge>();
			//Vector<Vertex> vertices = new Vector<Vertex>();
			
			//vertices.get(0).
			i_0 = 0;
			j_0 = 0;
			int k_0 = 0; // the 'name' of the program
			
			HashMap<String,Integer> hashy = new HashMap<String,Integer>();
			int key_count = 0;
			for(int i_1 = 0; i_1 < dependees.size(); i_1++) {
				
				for(int j_1 = 0; j_1 < dependees.get(i_1).size(); j_1++) {
					if(hashy.get(dependees.get(i_1).get(j_1)) == null) {
						hashy.put(dependees.get(i_1).get(j_1), key_count);
						key_count++;
					}
				}
			}
			
			// Has cycle ---> installable = false;
			System.out.println("dependees = "+dependees.toString());
			for(int i_1 = 0; i_1 < dependees.size(); i_1++) {
				for(int j_1 = 1; j_1 < dependees.get(i_1).size(); j_1++) {
					edges.add(new Edge(i_1,j_1));
					//graphy.getVertexSet().
					//Edge A = new Edge(i_1,j_1);
					//graphy.addEdge(hashy.get(dependees.get(i_1).get(0)), hashy.get(dependees.get(i_1).get(j_1)));
					//System.out.println("Connecting "+ hashy.get(dependees.get(i_1).get(0)) +" to "+hashy.get(dependees.get(i_1).get(j_1)));
				}
			}
			//if(graphy.isGraphCyclic()) {
			//	System.out.println("CONTAINS CYCLE");
			//	installable = false;
			//}

			graphy.setDirected(edges); // Okay, directed graph is established, now we must see if it is cyclic. This likely means we must build 'isCyclic' method(s) within Graph class.
			//System.out.println(graphy.toString());
			
			if(installable == true) {
				// "   " case
				if(commands.get(i).contains("   "))
					i++;
				
				// List case
				if(commands.get(i).contains("LIST")) {
					int i_1 = installed.size() - 1;
					while(i_1 >= 0) {
						commands.add(i+1,"   "+installed.get(i_1));
						i_1--;
					}
				}
				
				// Install case
				if(commands.get(i).contains("INSTALL")) {

					if(installed.contains(read(commands.get(i)).get(0))) {	// If already installed, will inform
						commands.add(i+1, "   "+read(commands.get(i)).get(0)+" is already installed");
					}
					else { // Just installs everything that is relevant
						int u=0;
						int catch_u = -1;
						while(u < dependees.size()) { // finds 
							if(dependees.get(u).get(0).contains(read(commands.get(i)).get(0))) {
								catch_u = u;
							}
							u++;
						}
						if(catch_u == -1) { // Relies on nothing, so we just install it
							commands.add(i+1, "   Installing "+read(commands.get(i)).get(0));
							installed.add(read(commands.get(i)).get(0)); // Check this later
							X++;
						}
						else { // Relies on some things, so we install what's necessary
							Vector<String> temp = new Vector<String>();
							int e = 0;
							int e_reverse = dependees.get(catch_u).size()-1;
							while(e < dependees.get(catch_u).size()) { 
								if(!(installed.contains(dependees.get(catch_u).get(e)))) {
									commands.add(i+1, "   Installing "+dependees.get(catch_u).get(e));
									temp.add(dependees.get(catch_u).get(e));
									//System.out.println(installed.toString());
								}
								X++;
								e++;
								e_reverse--;
							} 
							Collections.reverse(temp);
							installed.addAll(temp);
						}
						
					}
					
					// if doesn't depend on anything, then "   Installing X"
					if(!(installed.contains(read(commands.get(i)).get(0)))) { // Works
						commands.add(i+1,"   Installing "+ read(commands.get(i)).get(0));
						installed.add(read(commands.get(i)).get(0));
					}
				}
				
				// Remove case
				if(commands.get(i).contains("REMOVE")) {
					int index_0 = -1;

					if(!(installed.contains(read(commands.get(i)).get(0)))) {
						commands.add(i+1,"   "+read(commands.get(i)).get(0) +" is not installed");
					}
					
					else {
						int t_0 = 0, t_1;
						boolean relies = false;
						while(t_0 < dependees.size()) {
							t_1 = 1;
							while(t_1 < dependees.get(t_0).size()) {
								if(dependees.get(t_0).get(t_1).contains(read(commands.get(i)).get(0))) {
									relies = true;
								}
								t_1++;
							}
							t_0++;
						}
						
						
						t_0 = 0;
						while(t_0 < dependees.size()) {
							if(dependees.get(t_0).contains(read(commands.get(i)).get(0)))
								index_0 = t_0;
							t_0++;
						}

						if(relies == true) {
							commands.add(i+1,"   "+read(commands.get(i)).get(0) +" is still needed");
						}
						else { // Remove it and anything needed with it
							commands.add(i+1,"   Removing "+read(commands.get(i)).get(0));
							Collections.reverse(installed);
							installed.remove(read(commands.get(i)).get(0));
							Collections.reverse(installed);

							if(index_0 != -1) {
								
								int k00 = 1;
								Vector<String> temp = dependees.get(index_0);
								while(k00 < dependees.get(index_0).size()) {
									int i00 = 0;
									int j00;
									boolean relies_on_something_else = false;
									
									while(i00 < dependees.size()) {
										j00=1;
										if(i00 != index_0) {
											
											while(j00 < dependees.get(i00).size()) {
												if(dependees.get(index_0).get(k00).contains(dependees.get(i00).get(j00))) {
													relies_on_something_else = true;
												}
												j00++;
											}
										}
										i00++;
									}
									if(relies_on_something_else == false) {
										commands.add(i+2,"   Removing "+dependees.get(index_0).get(k00));
										Collections.reverse(installed);
										installed.remove(dependees.get(index_0).get(k00));
										Collections.reverse(installed);
									}
									k00++;
								}
								dependees.remove(index_0);
							}
						}
					}	
				}
			}
			i++;
		}
		int i_0 = 0, j_0;
		int X_0 = commands.size(); // So we can count X easily
		int counting = 0;
		X_0 = X_0 - commands.size();
		int sorted_size = 0;
		for(int i1 = 0; i1 < commands.size(); i1++) { // WORTHLESS 
			if(!(commands.get(i1).contains("   ")))
				sorted_size++;
		}
		int[] extra_lines = new int[sorted_size];
		
		int i0 = 0;
		int count = 0;
		while(i0 < commands.size()-1) { // Don't remove, it is needed
			int j0 = i0+1;
			while(commands.get(j0).contains("   ")) {
				j0++;
				extra_lines[count]++;
			}
			i0 = j0;
			count++;
		}
		int Q = 0;
		for(int q=0;q<LIMIT;q++) {
			Q = Q + extra_lines[q];
		}
		int pineapple = 0;
		while(pineapple < LIMIT + Q) { 
			System.out.println(commands.get(pineapple)); 
			pineapple++;
		}
	}
	
	public void runNCommandswCheckRecLarge (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands, checking for cycles and 
		//       recommending fix by removing largest cycle

		// TODO
	}

	public void runNCommandswCheckRecSmall (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands, checking for cycles and 
		//       recommending fix by removing smallest cycle

		// TODO
	}
	

	public Vector<String> readCommandsFromFile(String fInName) throws IOException {
		// PRE: -
		// POST: returns lines from input file as vector of string
		BufferedReader fIn = new BufferedReader(
							 new FileReader(fInName));
		String s;
		Vector<String> comList = new Vector<String>();
		
		while ((s = fIn.readLine()) != null) {
			comList.add(s);
		}
		fIn.close();
		
		return comList;
	}
	
	
	public String readSoln(String fInName, Integer N) throws IOException {
		// PRE: -
		// POST: returns N lines from input file as single string
		BufferedReader fIn = new BufferedReader(
							 new FileReader(fInName));
		String s;
		String out = "";
		Integer i = 0;

		while (((s = fIn.readLine()) != null) && (i <= N)) {
			if ((i != N) || s.startsWith("   ")) // responses to commands start with three spaces
				out += s + System.lineSeparator();
			if (!s.startsWith("   "))  
				i += 1;
		}
		fIn.close();
		
		return out;
	}


	public static void main(String[] args) {
		
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		String PATH = "C:\\Users\\User\\Documents\\Sample tests\\";
		// change to your own path
		
		Integer N = d.MAXCOMS; // originally d.MAXCOMS;
		//Integer N = 8;
		
		try {
			inCommands = d.readCommandsFromFile(PATH+"sample_P1.in"); // originally: "sample_D2a.in"
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		
		d.runNCommandswCheck(inCommands, N); // d.runNCommands(inCommands, N);
		System.out.println();

	}
	
	
	
	
	
	
	
	public class Edge {
		/*
		 * class Edge consists only of a pair of integers, to represent
		 * edges in a graph with vertices labelled with integers; there are
		 * corresponding get and set methods for those
		*/
		 
		private Integer first;
		private Integer second;
		
		public Edge(Integer f, Integer s) {
			first = f;
			second = s;
		}
		
		public Integer getFirst() {
			return first;
		}

		public Integer getSecond() {
			return second;
		}

		public void setFirst(int f) {
			first = f;
		}
		
		public void setSecond(int s) {
			second = s;
		}
	}

	
	public class Graph {

		/*
		 * Represents a graph using an adjacency list.
		 */
		
		private TreeMap<Integer, Vertex> m;
		// Graph implemented using a TreeMap: key is vertex id label,
		// value is the vertex object, which contains the adjacency list
		// for that vertex
		private Integer numEdges;
		private Iterator<Map.Entry<Integer, Vertex>> gIt;
		
		public Graph() {
			m = new TreeMap<Integer, Vertex>();
			numEdges = 0;
		}
		
		public Integer numVertices() {
			return m.size();
		}
		
		public Integer numEdges() {
			return numEdges;
		}
		
		public Vertex getVertex(Integer n) {
			// Used for accessing vertex to e.g. add neighbours
			return m.get(n);
		}
		
		public NavigableSet<Integer> getVertexSet() {
			// Returns set of all vertices in the graph
			return m.navigableKeySet();
		}
		
		public void addVertex(Integer n) {
			// Adds a new vertex n to the graph
			Vertex v = new Vertex(n);
			m.put(n, v);
		}
		
		public void deleteVertex(Integer n) {
			// Deletes a vertex n from the graph
			m.remove(n);  // Deletes vertex key from TreeMap
			gIt = m.entrySet().iterator(); // must come after remove
			// Also remove vertex n from list of neighbours
			while (gIt.hasNext()) {
				Map.Entry<Integer, Vertex> pairs = gIt.next();
				pairs.getValue().getAdjs().remove(n);
			}		
		}
		
		public void print() {
			gIt = m.entrySet().iterator();
			
			System.out.println("Number of nodes is " + m.size());
			System.out.println("Number of edges is " + numEdges);

			while (gIt.hasNext()) {
				Map.Entry<Integer, Vertex> pairs = gIt.next();
				pairs.getValue().print();
			}		
			System.out.println();
		}

		public void setUnmarked() {
			// Sets all vertices to be unmarked e.g. after traversal
			gIt = m.entrySet().iterator();
			while (gIt.hasNext()) {
				Map.Entry<Integer, Vertex> pairs = gIt.next();
				pairs.getValue().setUnmarked();
				pairs.getValue().setNum(0);
			}				
		}
		
		public Integer getFirstVertexID() {
			// Returns first vertex ID in TreeMap ordering
			// e.g. for starting a traversal
			return m.firstKey();
		}
		
		public boolean containsVertex(Integer n) {
			// Checks if n is a vertex in the graph
			return m.containsKey(n);
		}
		
		public void setDirected(Vector<Edge> eList) {
			// Instantiates a directed graph based on a list of edges
			m.clear();
			numEdges = eList.size();
			
			Iterator<Edge> eIt = eList.iterator();
			while (eIt.hasNext()) { // iterate through edges
				Edge curEdge = eIt.next();
				Integer key1 = curEdge.getFirst();
				Integer key2 = curEdge.getSecond();
				// Insert both vertices in the edge
				if (!m.containsKey(key1)) {
					Vertex v = new Vertex(key1);
					m.put(key1, v);
				}
				if (!m.containsKey(key2)) {
					Vertex v = new Vertex(key2);
					m.put(key2, v);
				}
				// Put second vertex in neighbour list of first vertex
				m.get(key1).addAdj(key2);
			}
		}
		
		public void setUndirected(Vector<Edge> eList) {
			// Instantiates an undirected graph based on a list of edges
			Vector<Edge> eList2 = (Vector<Edge>) eList.clone();
			Iterator<Edge> eIt = eList.iterator();
			// Extends list by reversing edges and adding them,
			// to get both directions (i.e. have symmetric adjacency lists)
			while (eIt.hasNext()) {
				Edge curEdge = eIt.next();
				eList2.add(new Edge(curEdge.getSecond(), curEdge.getFirst()));
			}	
			this.setDirected(eList2);
			
			numEdges = numEdges / 2;
		}
		
		public void setDefault () {
			// A sample undirected graph used in workshop questions
			Vector<Edge> eList = new Vector<Edge>();
			
			eList.add(new Edge(1,2));
			eList.add(new Edge(1,3));
			eList.add(new Edge(1,7));
			eList.add(new Edge(2,5));
			eList.add(new Edge(4,5));
			eList.add(new Edge(4,7));
			eList.add(new Edge(4,8));
			eList.add(new Edge(5,6));
			eList.add(new Edge(5,7));
			eList.add(new Edge(7,8));
			
			this.setUndirected(eList);
		}
		
		public void setDefaultDir () {
			// A sample directed graph used in workshop questions
			Vector<Edge> eList = new Vector<Edge>();
			
			eList.add(new Edge(1,2));
			eList.add(new Edge(1,4));
			eList.add(new Edge(2,3));
			eList.add(new Edge(2,5));
			eList.add(new Edge(4,5));
			eList.add(new Edge(5,3));
			eList.add(new Edge(5,6));
			eList.add(new Edge(7,4));
		
			this.setDirected(eList);
		}
		
		public Vector<Edge> readFromFile(String fInName) throws IOException {
			// Reads list of edges from a file, one pair of integers per line 
			BufferedReader fIn = new BufferedReader(
								 new FileReader(fInName));
			String s;
			Vector<Edge> eList = new Vector<Edge>();
			Integer x, y;
			
			while ((s = fIn.readLine()) != null) {
				java.util.StringTokenizer line = new java.util.StringTokenizer(s);
				while (line.hasMoreTokens()) {
					x = Integer.parseInt(line.nextToken());
					y = Integer.parseInt(line.nextToken());
					eList.add(new Edge(x,y));
				}
			}
			fIn.close();
			
			return eList;
		}
		
		public void main(String[] args) {
			Graph g = new Graph();
			Vector<Edge> eList;
			
			g.setDefault();
			g.print();
			g.setUnmarked();
			
			try {
				eList = g.readFromFile("C:/Users/mark/My Documents/madras/12comp225/mixed/wk9/graph-sample.txt");
				g.setUndirected(eList);
				g.print();
			}
			catch (IOException e) {
				System.out.println("in exception: " + e);
			}
			
			Set<Integer> s;
			s = g.getVertexSet();
			System.out.println(s);
			
		}
	}
	
	
	public class GraphApplic extends Graph {

		public void depthFirstTraversalRec1(Integer v) {
			// PRE: v is the id of a vertex in the graph
			// POST: Prints out a depth-first traversal of a graph
			//         (for just the connected component containing v)
			
			// Recursive version of DFT
			System.out.print(" " + v);
			getVertex(v).setMarked(); // get vertex object with id v,
			                          // indicate visited by setting marked
			VertexIDList adjList = getVertex(v).getAdjs();
			                          // get adjacency list representing neighbours
			Iterator<Integer> vIt = adjList.iterator();
			while (vIt.hasNext()) {   // iterate over neighbours
				Integer nextVertex = vIt.next();
				if (!getVertex(nextVertex).isMarked())  // if neighbour hasn't been visited
					depthFirstTraversalRec1(nextVertex); // visit it
			}
		}
		
		public List<Integer> depthFirstTraversalRec2(Integer v) {
			// PRE: v is the id of a vertex in the graph
			// POST: Returns a list containing a depth-first traversal of a graph
			//         (for just the connected component containing v)
			
			// Recursive version of DFT
			List<Integer> resList = new Vector<Integer>(); // list to hold DFT

			getVertex(v).setMarked(); // get vertex object with id v,
									  // indicate visited by setting marked
			VertexIDList adjList = getVertex(v).getAdjs();
									  // get adjacency list representing neighbours
			Iterator<Integer> vIt = adjList.iterator();
			while (vIt.hasNext()) {   // iterate over neighbours
				Integer nextVertex = vIt.next();
				if (!getVertex(nextVertex).isMarked()) {  // if neighbour hasn't been visited
					List<Integer> tmpResList = depthFirstTraversalRec2(nextVertex);
					                  // get traversal from recursive call
					resList.addAll(tmpResList);  // combine lists from recursive calls
				}
			}
			resList.add(0, v); // put current vertex at start of list
			return resList;
		}
		
		public void depthFirstTraversalIter1(Integer v) {
			// PRE: v is the id of a vertex in the graph
			// POST: Prints out a depth-first traversal of a graph
			//         (for just the connected component containing v)
			
			// Iterative version of DFT
			Stack<Integer> s = new Stack<Integer>();

			System.out.print(" " + v);
			s.push(v);
			getVertex(v).setMarked();
			
			while (!s.isEmpty()) { // while not all vertices visited
				v = s.peek();
				VertexIDList adjList = getVertex(v).getAdjs();
				Iterator<Integer> vIt = adjList.iterator();
								   // get iterator over adjacency list representing neighbours			
				while (vIt.hasNext() && getVertex(v).isMarked())
					v = vIt.next(); // skip over visited neighbours
			
				if (getVertex(v).isMarked()) {  // only occurs if all neighbours visited
					s.pop();       // remove from stack
				}
				else {             // v is an unvisited neighbour
					s.push(v);     // add to stack
					getVertex(v).setMarked();
					System.out.print(" " + v);	
				}
			}
		}



		public void main(String[] args) {
			GraphApplic g = new GraphApplic();
			List<Integer> l;
			
			g.setDefault();
			System.out.println("DFT #1");
			g.depthFirstTraversalRec1(g.getFirstVertexID());
			g.setUnmarked();
			System.out.println();
			
			System.out.println("DFT #2");
			l = g.depthFirstTraversalRec2(g.getFirstVertexID());
			g.setUnmarked();
			Iterator<Integer> lIt = l.iterator();
			while (lIt.hasNext())
				System.out.print(" " + lIt.next());
			System.out.println();

			System.out.println("DFT #3");
			g.depthFirstTraversalIter1(g.getFirstVertexID());
			g.setUnmarked();
			System.out.println();


		}
	}
	
	
	public class Vertex {
		/*
		 * class Vertex represents a vertex in a graph, containing the
		 * vertex label and other fields
		 * 
		 * contains a list of the vertex's neighbours, giving an adjacency
		 * list representation for the graph
		 */

		Integer id; // Vertex label
		VertexIDList adjs; // List of neighbours
		Boolean marked;  // Used to indicate previously visited
		Integer num;  // Used by Drozdek to indicate order of visit in traversal
		
		public Vertex(Integer n) {
			// Constructor
			id = n;
			marked = false;
			adjs = new VertexIDList();
			num = 0;
		}
		
		public void setMarked() {
			marked = true;
		}

		public void setUnmarked() {
			marked = false;
		}
		
		public boolean isMarked() {
			return marked;
		}
		
		public void addAdj (Integer n) {
			// Adds a neighbour n to the current vertex 
			adjs.push(n);
		}
		
		public VertexIDList getAdjs() {
			return adjs;
		}

		public Integer getID() {
			return id;
		}
		
		public Integer getNum() {
			return num;
		}
		
		public void setNum(Integer n) {
			num = n;
		}
		
		public void print() {
			System.out.print("Node " + id + " (" + marked + "," + num + ") :");
			adjs.print();
		}
		
		public void main(String[] args) {
			Vertex v = new Vertex(2);
			v.addAdj(3);
			v.addAdj(6);
			v.print();

			System.out.println("testing iterator ...");
			VertexIDList vAdjs = v.getAdjs();
			Iterator<Integer> it = vAdjs.iterator();
			while (it.hasNext()) {
				System.out.println(it.next());
			}
		}
	}
	
	
	public class VertexIDList extends TreeMap<Integer, Boolean> {

		/*
		 * List of vertices, to be used predominantly to represent
		 * the neighbours of a vertex
		 * 
		 * As an extension of TreeMap, inherits its methods, such
		 * as size()
		 */
		
		private Iterator<Map.Entry<Integer, Boolean>> it; 
		
		public VertexIDList() {
			// constructor
			super();
		}
		
		public Integer top() {
			// returns first element of list, treating as a queue
			return this.firstKey();
		}
		
		public void pop() {
			// deletes first element of list, treating as a queue
			this.remove(this.firstKey());
		}
		
		public void push(Integer n) {
			// inserts first element of list, treating as a queue
			this.put(n, true);
		}
		
		public void print() {
			it = this.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Boolean> pairs = it.next();
				System.out.print(" " + pairs.getKey());
			}		
			System.out.println();
		}
		
		public Iterator<Integer> iterator() {
			// returns an iterator to use to iterate over the list
			return this.keySet().iterator();
		}

		public void main(String[] args) {
			VertexIDList v = new VertexIDList();
			
			v.push(2);
			v.push(5);
			v.push(3);
			v.print();
			v.pop();
			System.out.println(v.top());
			v.print();
			
			Iterator<Integer> vIt = v.iterator();
			
			System.out.println("testing iterator ...");
			while (vIt.hasNext()) {
				System.out.println(vIt.next());
			}
			v.print();
		}
	}
	
	
}