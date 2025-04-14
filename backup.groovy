import java.text.SimpleDateFormat

// here's all our necessary variables
def sourceFolder = new File("/path/to/your/folder")  // Change this!
def timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())
def backupFolderName = "${sourceFolder.name}_backup_${timestamp}"
def backupFolder = new File(sourceFolder.parentFile, backupFolderName)

// Here is the code to actually copy the folder
def copyDir(File source, File dest) {
    if (!dest.exists()) dest.mkdirs()
    source.eachFileRecurse { file ->
        def target = new File(dest, file.path - source.path)
        if (file.isDirectory()) {
            target.mkdirs()
        } else {
            target.parentFile.mkdirs()
            file.withInputStream { input ->
                target.withOutputStream { output ->
                    output << input
                }
            }
        }
    }
}

// Here is where we ctually run
if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
    println "Invalid source folder: ${sourceFolder.absolutePath}"
}
