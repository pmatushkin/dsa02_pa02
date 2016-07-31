import java.io.*;
import java.util.PriorityQueue;
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

        // a min-heap of worker threads
        Worker[] workerHeap = new Worker[numWorkers];

        // an index of a head element in a heap
        // idea: populate heap from bottom to top,
        // use an index of a head element in the heap
        // as a starting point of the process of sifting down,
        // thus cutting down the processing time
        // a note on the initial value: don't decrement by 1 here,
        // because its value will be decremented before addition of a new worker thread
        int heapHead = numWorkers;

        // a current time counter to use when creating the new threads
        long currentTime = 0;

        for (int i = 0; i < jobs.length; i++) {
            // decide whether to use an existing worker thread,
            // or create a new one.
            if (heapHead != 0) {
                // there are available threads still
                // AND we're past the initialization, so we can check when the top thread becomes available
                // and the current thread becomes available later than the current time
                if (!(heapHead < numWorkers && workerHeap[heapHead] != null && workerHeap[heapHead].nextFreeTime == currentTime)) {
                    // creating a new thread and adding to a heap
                    heapHead--;
                    workerHeap[heapHead] = new Worker(numWorkers - 1 - heapHead, currentTime);
                }
            }

            Worker bestWorker = workerHeap[heapHead];

            // write down the output values
            assignedWorker[i] = bestWorker.id;
            startTime[i] = bestWorker.nextFreeTime;

            // move the current time
            currentTime += bestWorker.nextFreeTime;

            // compute the next free time
            bestWorker.nextFreeTime += jobs[i];

            // rearrange the elements of min-heap
            SiftDown(workerHeap, heapHead);
        }
    }

    private void SiftDown(Worker[] workerHeap, int heapHead) {
//        boolean isHeapRearranged = false;

        int i = heapHead;

        while (true) {
            if (i == numWorkers - 1) {
                return;
//                isHeapRearranged = true;
            } else {
//                Worker workerHeap_i = workerHeap[i];
//                Worker workerHeap_i_next = workerHeap[i + 1];
//
//                int i_id = workerHeap_i.id;
//                long i_nextFreeTime = workerHeap_i.nextFreeTime;
//
//                int i_next_id = workerHeap_i_next.id;
//                long i_next_nextFreeTime = workerHeap_i_next.nextFreeTime;

//                Worker workerHeap_i = workerHeap[i];
//                Worker workerHeap_i_next = workerHeap[i + 1];

                int i_id = workerHeap[i].id;
                long i_nextFreeTime = workerHeap[i].nextFreeTime;

                int i_next_id = workerHeap[i + 1].id;
                long i_next_nextFreeTime = workerHeap[i + 1].nextFreeTime;

                if (i_nextFreeTime > i_next_nextFreeTime) {
                    workerHeap[i].id = i_next_id;
                    workerHeap[i].nextFreeTime = i_next_nextFreeTime;

                    workerHeap[++i].id = i_id;
                    workerHeap[i].nextFreeTime = i_nextFreeTime;
                } else {
                    if (i_nextFreeTime < i_next_nextFreeTime) {
                        return;
//                        isHeapRearranged = true;
                    } else {
                        if (i_id < i_next_id) {
                            return;
//                            isHeapRearranged = true;
                        } else {
                            workerHeap[i].id = i_next_id;
                            workerHeap[i].nextFreeTime = i_next_nextFreeTime;

                            workerHeap[++i].id = i_id;
                            workerHeap[i].nextFreeTime = i_nextFreeTime;
                        }
                    }
                }
            }
        }
    }

    private void SiftDown_Works(Worker[] workerHeap, int heapHead) {
        boolean isHeapRearranged = false;

        int i = heapHead;

        while (!isHeapRearranged) {
            if (i == numWorkers - 1) {
                isHeapRearranged = true;
            } else {
                Worker workerHeap_i = workerHeap[i];
                Worker workerHeap_i_next = workerHeap[i + 1];

                if (workerHeap_i.nextFreeTime > workerHeap_i_next.nextFreeTime) {
                    workerHeap[i] = workerHeap_i_next;
                    workerHeap[i + 1] = workerHeap_i;

                    i++;
                } else {
                    if (workerHeap_i.nextFreeTime < workerHeap_i_next.nextFreeTime) {
                        isHeapRearranged = true;
                    } else {
                        if (workerHeap_i.id < workerHeap_i_next.id) {
                            isHeapRearranged = true;
                        } else {
                            workerHeap[i] = workerHeap_i_next;
                            workerHeap[i + 1] = workerHeap_i;

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
