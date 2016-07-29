import java.io.*;
import java.util.StringTokenizer;

public class JobQueue {
    private int numWorkers;
    private int[] jobs;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    class Worker {
        public Worker(int id, long nextFreeTime) {
            this.id = id;
            this.nextFreeTime = nextFreeTime;
        }

        public int id;
        public long nextFreeTime;
    }

    private void assignJobs() {
        // TODO: replace this code with a faster algorithm.
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];

        numWorkers = Math.min(numWorkers, jobs.length);

        // a min-heap of worker threads
        Worker[] workerHeap = new Worker[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            workerHeap[i] = new Worker(i, 0);
        }

        for (int i = 0; i < jobs.length; i++) {
            // read the best available working thread object
            // it's always at the top of the min-heap
            Worker bestWorker = workerHeap[0];

            // write down the output values
            assignedWorker[i] = bestWorker.id;
            startTime[i] = bestWorker.nextFreeTime;

            // compute the next free time
            bestWorker.nextFreeTime += jobs[i];

            // rearrange the elements of min-heap
            SiftDown(workerHeap);
        }
    }

    // not finished
    private void assignJobsAllocateDynamically() {
        // TODO: replace this code with a faster algorithm.
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];

        // a min-heap of worker threads
        Worker[] workerHeap = new Worker[numWorkers];

        // a number of still available threads
        int numAvailableWorkers = numWorkers;

        // a next free time value to use
        // while there are still workers not in the heap
        // (when all the workers are added to the heap,
        // we'll be using Worker.nextFreeTime values)
        long nextFreeTime = 0;

        for (int i = 0; i < jobs.length; i++) {
            int heapWorkerId = -1;
            long heapNextFreeTime = -1;

            int jobDuration = jobs[i];

            if (null == workerHeap[0]) {
                Worker worker = new Worker(0, jobDuration);
                workerHeap[0] = worker;
                numAvailableWorkers--;

                assignedWorker[0] = 0;
                startTime[0] = 0;
            } else {
                heapWorkerId = workerHeap[0].id;
                heapNextFreeTime = workerHeap[0].nextFreeTime;

                Worker nextWorker;

                // check if we can schedule the next thread immediately
                if (numAvailableWorkers > 0 && nextFreeTime < heapNextFreeTime) {
                    nextWorker = new Worker(numWorkers - numAvailableWorkers, nextFreeTime + jobDuration);
                    workerHeap[numWorkers - numAvailableWorkers] = nextWorker;

                    numAvailableWorkers--;

                    SiftUp(numWorkers - numAvailableWorkers, workerHeap);
                } else {

                }

                SiftDown(workerHeap);
            }
        }
    }

    private void SiftUp(int i, Worker[] workerHeap) {
    }

    private void SiftDown(Worker[] workerHeap) {
        boolean isHeapRearranged = false;

        int i = 0;

        while (!isHeapRearranged) {
            if (i == numWorkers - 1) {
                isHeapRearranged = true;
            } else {
                if (workerHeap[i].nextFreeTime > workerHeap[i + 1].nextFreeTime) {
                    Worker temp = workerHeap[i];
                    workerHeap[i] = workerHeap[i + 1];
                    workerHeap[i + 1] = temp;

                    i++;
                } else {
                    if (workerHeap[i].nextFreeTime < workerHeap[i + 1].nextFreeTime) {
                        isHeapRearranged = true;
                    } else {
                        if (workerHeap[i].id < workerHeap[i + 1].id) {
                            isHeapRearranged = true;
                        } else {
                            Worker temp = workerHeap[i];
                            workerHeap[i] = workerHeap[i + 1];
                            workerHeap[i + 1] = temp;

                            i++;
                        }
                    }
                }
            }
        }
    }

    private void assignJobsNaive() {
        // TODO: replace this code with a faster algorithm.
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
        long[] nextFreeTime = new long[numWorkers];
        for (int i = 0; i < jobs.length; i++) {
            int duration = jobs[i];
            int bestWorker = 0;
            for (int j = 0; j < numWorkers; ++j) {
                if (nextFreeTime[j] < nextFreeTime[bestWorker])
                    bestWorker = j;
            }
            assignedWorker[i] = bestWorker;
            startTime[i] = nextFreeTime[bestWorker];
            nextFreeTime[bestWorker] += duration;
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobs();
        writeResponse();
        out.close();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
