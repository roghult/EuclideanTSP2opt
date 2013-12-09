import java.util.Random;


public class NearestNeighbor {

	public static int[] calcRoute(int N) {
		int startNode = new Random().nextInt(N);
		int[] route = new int[N];
		route[0] = startNode;
		boolean[] visited = new boolean[N];

		visited[startNode] = true;
		int node = startNode;

		for (int i = 0; i < N; i++) {
			int nearestNeighbor = startNode;
			int closestDistance = Integer.MAX_VALUE;
			for (int j = 0; j < N; j++) {
				if (!visited[j]) {
					int distance = Main.distances[node][j];
					if (distance < closestDistance) {
						closestDistance = distance;
						nearestNeighbor = j;
					}
				}
			}
			route[i] = nearestNeighbor;
			node = nearestNeighbor;
			visited[nearestNeighbor] = true;
		}
		return route;
	}

}
