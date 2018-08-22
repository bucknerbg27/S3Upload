import java.io.File;
import java.util.ArrayList;



public class PutToS3 {

    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    PutToS3 [--recursive] [--pause] <s3_path> <local_paths>\n\n" +
                "Where:\n" +
                "    --recursive - Only applied if local_path is a directory.\n" +
                "                  Copies the contents of the directory recursively.\n\n" +
                "    --pause     - Attempt to pause+resume the upload. This may not work for\n" +
                "                  small files.\n\n" +
                "    s3_path     - The S3 destination (bucket/path) to upload the file(s) to.\n\n" +
                "    local_paths - One or more local paths to upload to S3. These can be files\n" +
                "                  or directories. Globs are permitted (*.xml, etc.)\n\n" +
                "Examples:\n" +
                "    PutToS3 hs-filesystem-prod/setupSchedule.exe my_programs/setupSchedule13.exe\n" +
                "    PutToS3 hs-filesystem-prod my_programs/setupSchedule13.exe\n" +
                "    PutToS3 hs-filesystem-prod my_programs/setup*.exe\n" +
                "    PutToS3 hs-filesystem-prod my_programs\n\n";


        if (args.length < 2) {
            System.out.println(USAGE);
            System.exit(1);
        }

        int cur_arg = 0;
        boolean recursive = false;
        boolean pause = false;
        UploadMgr uploadMgr = new UploadMgr();

        // first, parse any switches
        while (args[cur_arg].startsWith("--")) {
            if (args[cur_arg].equals("--recursive")) {
                recursive = true;
            } else if (args[cur_arg].equals("--pause")) {
                pause = true;
            } else {
                System.out.println("Unknown argument: " + args[cur_arg]);
                System.out.println(USAGE);
                System.exit(1);
            }
            cur_arg += 1;
        }

        // only the first '/' character is of interest to get the bucket name.
        // Subsequent ones are part of the key name.
        String s3_path[] = args[cur_arg].split("/", 2);
        cur_arg += 1;

        // Any remaining args are assumed to be local paths to copy.
        // They may be directories, arrays, or a mix of both.
        ArrayList<String> dirs_to_copy = new ArrayList<String>();
        ArrayList<String> files_to_copy = new ArrayList<String>();

        while (cur_arg < args.length) {
            // check to see if local path is a directory or file...
            File f = new File(args[cur_arg]);
            if (f.exists() == false) {
                System.out.println("Input path doesn't exist: " + args[cur_arg]);
                System.exit(1);
            }
            else if (f.isDirectory()) {
                dirs_to_copy.add(args[cur_arg]);
            } else {
                files_to_copy.add(args[cur_arg]);
            }
            cur_arg += 1;
        }

        String bucket_name = s3_path[0];
        String key_prefix = null;
        if (s3_path.length > 1) {
            key_prefix = s3_path[1];
        }

        // Upload any directories in the list.
        for (String dir_path : dirs_to_copy) {
            uploadMgr.uploadDir(dir_path, bucket_name, key_prefix, recursive, pause);
        }

        // If there's more than one file in the list, upload it as a file list.
        // Otherwise, upload it as a single file.
        if (files_to_copy.size() > 1) {
            UploadMgr.uploadFileList(files_to_copy.toArray(new String[0]), bucket_name,
                    key_prefix, pause);
        } else if (files_to_copy.size() == 1) {
            uploadMgr.uploadFile(files_to_copy.get(0), bucket_name, key_prefix, pause);
        } // else: nothing to do.
    }
}



