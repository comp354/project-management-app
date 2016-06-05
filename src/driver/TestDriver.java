package driver;

/*import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;

import resources.Activities;

public class TestDriver {
	
// only used for testing
	
/*
	public static void main(String[] args) {
		
		Activities A = new Activities(0, "S", 14, 0, 0);
		Activities B = new Activities(1, "DCA", 5, 1, 1);
		Activities C = new Activities(2, "DCB", 10, 2, 2);
		Activities D = new Activities(3, "DCC", 10, 3, 3);
		Activities E = new Activities(4, "UTAB", 14, 4, 4);
		Activities F = new Activities(5, "UTC", 8, 5, 5);
		Activities G = new Activities(6, "P", 10, 6, 6);
		Activities H = new Activities(7, "IST", 21, 7, 7);
		
		Users[] members = new Users[5];
		Projects tester = new Projects(123, "Test Project", members, "May 26 2016");
		
		tester.addActivity(A);
		tester.addActivity(B);
		tester.addActivity(C);
		tester.addActivity(D);
		tester.addActivity(E);
		tester.addActivity(F);
		tester.addActivity(G);
		tester.addActivity(H);
		
		tester.addArrow(A, B);
		tester.addArrow(A, C);
		tester.addArrow(A, D);
		tester.addArrow(B, E);
		tester.addArrow(C, E);
		tester.addArrow(D, F);
		tester.addArrow(E, G);
		tester.addArrow(F, G);
		tester.addArrow(G, H);
		
		tester.calculateTimes();
			
		Set<Activities> vertices = tester.getActivityGraph().vertexSet();
		Set<DefaultEdge> edges = tester.getActivityGraph().edgeSet();
		
		for(Activities e : vertices)
		{
			System.out.println("ACTIVITY: " + e.getId() + " " + e.getDescription());
			System.out.println("Connected to: ");
			Set<DefaultEdge> edgelist = tester.getOutgoingArrowsOfActivity(e);
			if (edgelist.size() == 0)
				System.out.println("None! Last activity!");
			else
			{
				for(DefaultEdge f : edgelist)
				{
					System.out.println(tester.getActivityAfter(f).getId() + " " + tester.getActivityAfter(f).getDescription());
				}
				System.out.println("-------------------------");
				System.out.println("Incoming edges from: ");
			}
			edgelist = tester.getIncomingArrowsOfActivity(e);
			if (edgelist.size() == 0)
				System.out.println("None! First activity!");
			else
			{
				for(DefaultEdge f : edgelist)
				{
					System.out.println(tester.getActivityBefore(f).getId() + " " + tester.getActivityBefore(f).getDescription());
				}
			}
			System.out.println("-------------------------");
			System.out.println("Activity Stats after forward Pass:");
			System.out.println("ES: " + tester.getES(e));
			System.out.println("Duration: " + e.getDuration());
			System.out.println("EF: " + tester.getEF(e));
			System.out.println("--------------------------------------------------------");
		}
	
		

	}

}*/
