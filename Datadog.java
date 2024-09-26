import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Datadog {
    public static void main (String... args) {
        System.out.println("starting...");
        // HighPerformanceFilter hpf = new HighPerformanceFilter();
        // hpf.insertTag("apple, facebook, google");
        // hpf.insertTag("banana, facebook");
        // hpf.insertTag("facebook, google, tesla");
        // hpf.insertTag("intuit, google, facebook");
        
        // List<String> ans = hpf.searchTags(Arrays.asList(new String[] {"facebook", "google"}));
        // for (String s : ans) System.out.print(s + ", ");
        // System.out.println();
        // BufferedFile bf = new BufferedFile("test.txt", 20);
        // bf.write("Hassan, ".getBytes());
        // bf.write("is awesome!!".getBytes());
        String[] livetail_stream = new String[] {
            "Q: database",
            "Q: Stacktrace",
            "Q: loading failed",
            "L: Database service started",
            "Q: snapshot loading",
            "Q: fail",
            "L: Started processing events",
            "L: Loading main DB snapshot",
            "L: Loading snapshot failed no stacktrace available"
        };

        StreamChecker sc = new StreamChecker();

        for (String s : livetail_stream) {
            String output = sc.processLine(s);
            if (output.length() > 0)
                System.out.println(output);
        }
    }
}

/**
     * Q1: There is a stream that has coming tags and also has a list of keywords. Design a high
     * performance filter to output these keywords in the remaining tags.
     * For example: given ['apple, facebook, google', 'banana, facebook', 'facebook, google, tesla', 'intuit, google, facebook']
     * if the keyboard is 'apple' the output should be only ['facebook', 'google'] because only the first string has apple in it.
     * Similarly, if the keywords are ['facebook', 'google'] then the output should be ['apple','tesla','intuit'].
     * The return output can be any order and can be put into a single list. 
     * 
     * NOTE: make sure to optimize based on:
     * 1. High performance filter - this means do not take in an array of strings, but just individual strings
     * 2. Imagine the tags are coming in as a stream.
     * 
     * source: https://leetcode.com/discuss/interview-question/2639509/DataDog-Interview-Question
     * 
     * my own follow-up: how would you make it to filter multiple times?
     * - you could use 2 filters separately then find the intersection of the results.
*/
class HighPerformanceFilter {
    // Note 1: if tags are coming in a streaming fashion, we need to store them in some data-structure
    // I will use a list
    private List<String> stream;
    
    // Note 2: assume that filters are not saved, but input to the function. to search and filter our tags.

    // To optimize, add a map because that is too costly to traverse through for every tag.
    // The map will store <word,set of indices of the tag of the word>
    Map<String,Set<Integer>> streamMap;

    public HighPerformanceFilter() {
        stream = new ArrayList<>();
        streamMap = new HashMap<>();
    }

    /**
     * For each word in the tag, add the index of **where** it is stored in the stream
     * and add the tag to the stream as well.
     * Time: O(m) where m is the number of words in tag
     */
    public void insertTag(String tag) {
        int idx = stream.size();
        for (String word : tag.split(",")) {
            word = word.trim().toLowerCase();
            streamMap.putIfAbsent(word, new HashSet<>());
            streamMap.get(word).add(idx);
        }
        stream.add(tag);
    }

    // Time: O(k*c) where k is the number of keywords and c is the count of matching tags
    public List<String> searchTags(List<String> keywords) {
        // <index of matched tag in streamMap : count of how many times a keyword matched>
        Map<Integer,Integer> counterMap = new HashMap<>();

        for (String kw : keywords) {
            // get the set of indices of all tags that match this keywork, or empty if none do
            Set<Integer> matchedTags = streamMap.getOrDefault(kw, new HashSet<>());
            // iterate through the matched tag indices and increment counter
            for (int document : matchedTags) {
                // <index of matched tag index : how many tags 
                counterMap.put(document, counterMap.getOrDefault(document, 0)+1);
            }
        }

        Set<String> matched = new HashSet<>();
        for (int key : counterMap.keySet()) {
            // only if **all** words match add to set
            if (counterMap.get(key) == keywords.size()) {
                matched.addAll(Arrays.asList(stream.get(key).split(", ")));
            }
        }
        // remove the keywords from the set, set makes it easy to do this fast
        for (String kw : keywords) {
            matched.remove(kw);
        }

        return new ArrayList<>(matched);
    }

    // // Time: O(n*m) where n is length of this.tags and m is the number of words in a tag
    // public List<String> searchTags(List<String> keywords) {
    //     List<String> res = new ArrayList<>();
    //     for (String tag : this.tags) {
    //         // for each tag, check if any of the words match the key words
    //         String[] tagWords = tag.split(", ");
    //         // get the words that match for this tag
    //         Set<String> matchedWords = getMatches(tagWords, keywords);
    //         if (matchedWords.isEmpty() || matchedWords.size() != keywords.size()) continue; // no matching words
    //         // add the words in tagWords to res if they are not in the set
    //         for (String tw : tagWords) {
    //             if (!matchedWords.contains(tw))
    //                 res.add(tw);
    //         }
    //     }
    //     return res;
    // }
    // // Time: O(m*k) where m is length of tagWords and k for keywords
    // private Set<String> getMatches(String[] tagWords, List<String> keyWords) {
    //     // we want to return a set of the words that match
    //     Set<String> matchedWords = new HashSet<>();
    //     for (String kw : keyWords) {
    //         for (String tw : tagWords) {
    //             if (kw.equals(tw))
    //                 matchedWords.add(kw);
    //         }
    //     }
    //     return matchedWords;
    // }

}

/**
 * Given the following input and output:
 * livetail_stream = [
  "Q: database",
  "Q: Stacktrace",
  "Q: loading failed",
  "L: Database service started",
  "Q: snapshot loading",
  "Q: fail",
  "L: Started processing events",
  "L: Loading main DB snapshot",
  "L: Loading snapshot failed no stacktrace available",
]

livetail_output = [
  "ACK: database; ID=1",
  "ACK: Stacktrace; ID=2",
  "ACK: loading failed; ID=3",
  "M: Database service started; Q=1",
  "ACK: snapshot loading; ID=4",
  "ACK: fail; ID=5",
  "M: Loading main DB snapshot; Q=4",
  "M: Loading snapshot failed no stacktrace available; Q=2,3,4",
]

the efficient approach is to build an inverted index out of logs, search for each 
query string, and fill a resultant list in whatever way desired.

Example pseudo code:

    Build an inverted index hashmap HM<string, list>. Eg: L1: "database service started" -> database - {1}, service- {1}, started- {1}
    Initialise the resultant list res
    For each query_idx, query
    -----for each string in the query
    --------List IDs = HM.get(string)
    --------for each id in IDs
    ----------freq[id]++;
    ----for each id, count in freq
    -------if count == query.size()
    ---------res.get(id).add(query_idx);
    Return res;
 
 * For every query, add the contents to the logs. For every log, see if there are matches
 * and show the matching query.
 */

class StreamChecker {
    private Map<String,Set<Integer>> streamMap;  // inverted index of logs
    private int id;  // id of the newer query being read

    public StreamChecker() {
        streamMap = new HashMap<>();
        id = 0;
    }

    public String processLine(String line) {
        char type = line.charAt(0);
        if (type == 'Q') {
            // add to our hashmap
            this.id++;
            String query = line.substring(3);
            String[] words = query.split(" ");
            for (String w : words) {
                w = w.trim().toLowerCase();
                streamMap.putIfAbsent(w, new HashSet<>());
                streamMap.get(w).add(id);
            }
            return "ACK: " + query + "; ID=" + this.id;
        }
        else if (type == 'L') {
            // return all queries that match
            String query = line.substring(3);
            String[] words = query.split(" ");
            Set<Integer> foundQueryIdx = new HashSet<>();
            for (String w : words) {
                w = w.trim().toLowerCase();
                if (streamMap.containsKey(w)) {
                    foundQueryIdx.addAll(streamMap.get(w));
                }
            }
            return foundQueryIdx.isEmpty() ? "" : "M: " + query + "; Q=" + foundQueryIdx.toString();
        }
        return "";
    }
}

/**
 * Your task is to create a new file type with a “Buffer”.
 * Every time you write data to this file, you use the Write method like write(“hello world”), 
 * but if you have enough buffer in memory (buffer_size), you write to buffer first. Only write 
 * to disk when the buffer is full (using the flush method to write to disk).
 * Implement both the Write and flush functions
 */
class BufferedFile {
    private File file;

    private int bufferSize;

    private int bufferIdx;  // this is the current free buffer idx.

    private byte[] buffer;

    
    // initialize based on the buffer size and create **new** empty file
    public BufferedFile(String fileName, int bufferSize) {
        this.file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error creating the file.");
        }
        this.bufferSize = bufferSize;
        this.bufferIdx = 0;
        this.buffer = new byte[bufferSize];
    }
    
    public void write(byte[] input) {
        /* TODO: check if input is bigger than buffer, then flush current buffer
         * and then flush the input directly to file? Or ignore? These are q's to 
         * as the interviewer! */

        // check if the size of the byte array + index is within bounds, if not
        // then flush the array. 
        if (this.bufferIdx + input.length > this.bufferSize) {
            flush();
        }
        // write to in-memory buffer
        for (int i=0; i<input.length; i++) {
            this.buffer[this.bufferIdx++] = input[i];
        }
        // check if buffer is full now and flush if it is
        if (this.bufferIdx == this.bufferSize) {
            flush();
        }

    }

    public void flush() {
        System.out.println("flushing file...");
        Path fileToWrite = Paths.get(this.file.getName());
        try {
            Files.write(fileToWrite, this.buffer);
            this.bufferIdx = 0;
        }
        catch (IOException ex) {
            System.out.println("Failed to flush to the file!");
        }
    }
}

/**
 * Assuming you have these functions:

    Delete(path) -> bool: deletes the file or empty directory
    IsDirectory(path) -> bool: checks whether filepath is directory or not
    GetAllFiles(path) -> List<string>: gets the absolute paths of all files in a directory, including other directories
 * Implement rm -rf.
 * define DeleteAllFilesAndDir(path):
 * 
 * How do you code it in a way that prevents out of memory (OOM) errors?
 */
class DeleteRm {

    public void delete(String path) {
        System.out.println("Deleting file/directory in: " + path);
    }

    public boolean isDirectory(String path) {
        if (path.length() % 2 == 0) return false;
        return true;
    }

    public List<String> getAllFiles(String path) {
        System.out.println("Getting all files for: " + path);
        return new ArrayList<String>();
    }

    /* solution 1: recursive
     * The problem with this is that if the directory is '/'
     * then we will get out of memory error or stack overflow.
     * This approach although logical not practical
     */ 
    public void deleteAllFilesAndDir_BAD(String path) {
        for (String filePath : getAllFiles(path)) {
            if (isDirectory(filePath)) {
                deleteAllFilesAndDir_BAD(filePath);
            } 
            else {
                delete(filePath);
            }
        }
    }

    /**
     * solution 2: iterative
     * At first glance, this may seem like the same solution, but
     * iterative, which is true. However, with a stack we can define
     * a certain size as the max size and if it is reached, then
     * check the stack for any files and delete them all, after that
     * continue. Another way can be to have a file stack and keep 
     * pushing to that, then at the end delete all files.
     */
    public void deleteAllFilesAndDir(String path) {
        Stack<String> stack = new Stack<>();
        stack.push(path);
        while (!stack.isEmpty()) {
            List<String> filePaths = getAllFiles(stack.pop());
            for (String fileStr : filePaths) {
                if (isDirectory(fileStr)) {
                    stack.push(fileStr);
                }
                else {
                    delete(fileStr);
                }
            }
        }
    }
}