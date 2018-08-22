# S3Upload
Java program to upload to S3 buckets. This has been pulled for AWS website and slightly modified.

Usage:
from a command line:
                "    S3Upload [--recursive] [--pause] <s3_path> <local_paths>\n\n" +
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

