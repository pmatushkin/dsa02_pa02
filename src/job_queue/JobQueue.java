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

        if (numWorkers > jobs.length) {
            numWorkers = jobs.length;
        }

        // a min-heap of worker threads
        Worker[] workerHeap = new Worker[numWorkers];

        // a max index of a head element in a heap
        int maxHeapHead = numWorkers - 1;
        // an index of a head element in a heap
        int heapHead = maxHeapHead;

        // a current time counter to use when creating the new threads
        long currentTime = 0;

        // initialize the head of the heap
        workerHeap[heapHead] = new Worker(0, currentTime);

        Worker bestThread;

        for (int i = 0; i < jobs.length; i++) {
            if (heapHead == 0) {
                bestThread = workerHeap[0];
            } else {
                // create a new worker thread if possible
                if (workerHeap[heapHead].nextFreeTime > currentTime) {
                    heapHead--;
                    bestThread = new Worker(maxHeapHead - heapHead, currentTime);
                    workerHeap[heapHead] = bestThread;
                } else {
                    bestThread = workerHeap[heapHead];
                }
            }

            long bestNextFreeTime = bestThread.nextFreeTime;

            // write down the output values
            assignedWorker[i] = bestThread.id;
            startTime[i] = bestNextFreeTime;

            // move the current time
            currentTime += bestNextFreeTime;

            // compute the next free time
            bestThread.nextFreeTime = bestNextFreeTime + jobs[i];

            if (heapHead == maxHeapHead) {
                continue;
            }

            // rearrange the elements of min-heap
            SiftDown(workerHeap, heapHead);
        }
    }

    private void SiftDown(Worker[] workerHeap, int heapHead) {
        int i = heapHead;

        while (true) {
            if (i == numWorkers - 1) {
                return;
            } else {
                int i_id = workerHeap[i].id;
                long i_nextFreeTime = workerHeap[i].nextFreeTime;

                int i_next_id = workerHeap[i + 1].id;
                long i_next_nextFreeTime = workerHeap[i + 1].nextFreeTime;

                if (i_nextFreeTime > i_next_nextFreeTime) {
                    workerHeap[i].id = i_next_id;
                    workerHeap[i].nextFreeTime = i_next_nextFreeTime;

                    workerHeap[++i].id = i_id;
                    workerHeap[i].nextFreeTime = i_nextFreeTime;
                } else if (i_nextFreeTime < i_next_nextFreeTime) {
                    return;
                } else {
                    if (i_id > i_next_id) {
                        workerHeap[i].id = i_next_id;
                        workerHeap[i].nextFreeTime = i_next_nextFreeTime;

                        workerHeap[++i].id = i_id;
                        workerHeap[i].nextFreeTime = i_nextFreeTime;
                    } else {
                        return;
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
