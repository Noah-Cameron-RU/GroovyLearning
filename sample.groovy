def source = new File("C:/temp/source.txt")
def destination = new File("C:/temp/archive/source.txt")

// Copy the file
destination.bytes = source.bytes

println "File copied successfully!"
