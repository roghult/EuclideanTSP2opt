import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;

public class Main {

	private static double[][] nodes;
	public static int[][] distances;
	private static int N;
	private static final long TIME_LIMIT = 1450;

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		long startTime = new Date().getTime();

		// read input
		readInput();

		long deadline = startTime + TIME_LIMIT;

		// calculate route using nearest neighbor
		int[] route = NearestNeighbor.calcRoute(N);
		
		int[] tempRoute = new int[N];
		for (int i = 0; i < N; i++) {
			tempRoute[i] = route[i];
		}

		int routeLength = calcRouteLength(route);

		while (new Date().getTime() < deadline) {
			// shuffle edges so that a -> b -> c -> d becomes a -> c -> b -> d
			// in hope of finding a better oute when using 2-opt. randomly
			// decide a.
			if (N > 4) {
				randomlyShuffleEdges(tempRoute, 60);
			}
		
			// run 2-opt until no more improvement can be made
			boolean edgeSwapped = true;
			while (edgeSwapped) {
				edgeSwapped = false;
				for (int i = 1; i < N - 1; i++) {
					for (int k = i + 1; k < N - 1; k++) {
						int oldDistance = distances[tempRoute[i - 1]][tempRoute[i]]
								+ distances[tempRoute[k]][tempRoute[k + 1]];
						int newDistance = distances[tempRoute[i - 1]][tempRoute[k]]
								+ distances[tempRoute[i]][tempRoute[k + 1]];
						if (newDistance < oldDistance) {
							twoOptSwap(tempRoute, i, k);
							edgeSwapped = true;
						}
					}
				}
			}
			int tempRouteLength = calcRouteLength(tempRoute);
			if (tempRouteLength < routeLength) {
				routeLength = tempRouteLength;
				route[0] = tempRoute[1];
				route[N - 1] = tempRoute[0];
				for (int i = 1; i < N - 1; i++) {
					route[i] = tempRoute[i + 1];
				}
			}
		}

		printRoute(route);
	}

	private static void twoOptSwap(int[] route, int i, int k) {
		int l = k;
		int limit = i + ((k - i) / 2);
		for (int j = i; j <= limit; j++) {
			int temp = route[j];
			route[j] = route[l];
			route[l] = temp;
			l--;
		}
	}

	private static void randomlyShuffleEdges(int[] route, int numberOfShuffles) {
		Random rand = new Random();
		for (int i = 0; i < numberOfShuffles; i++) {
			int a = rand.nextInt(N - 1);
			int b = a + 1;
			int temp = route[a];
			route[a] = route[b];
			route[b] = temp;
		}
	}

	private static void readInput() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		double x;
		double y;
		nodes = new double[N][2];
		distances = new int[N][N];
		String[] coordinates;
		for (int i = 0; i < N; i++) {
			coordinates = br.readLine().split(" ");
			x = Double.parseDouble(coordinates[0]);
			y = Double.parseDouble(coordinates[1]);
			nodes[i][0] = x;
			nodes[i][1] = y;

			for (int j = 0; j < i; j++) {
				int distance = calcDistance(i, j);
				distances[i][j] = distance;
				distances[j][i] = distance;
			}
		}
	}

	private static int calcRouteLength(int[] route) {
		int distance = 0;
		for (int i = 0; i < route.length - 1; i++) {
			distance += distances[route[i]][route[i + 1]];
		}
		return distance;
	}

	private static void printRoute(int[] route) {
		for (int i = 0; i < route.length; i++) {
			System.out.println(route[i]);
		}
	}

	public static int calcDistance(int i, int j) {
		return (int) (Math
				.sqrt(((nodes[i][0] - nodes[j][0]) * (nodes[i][0] - nodes[j][0]))
						+ ((nodes[i][1] - nodes[j][1]) * (nodes[i][1] - nodes[j][1]))) + 0.5d);
	}
}