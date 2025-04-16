@Grab(group='org.apache.poi', module='poi-ooxml', version='5.2.3')

import org.apache.poi.xssf.usermodel.*
import java.text.SimpleDateFormat

// Run the Windows event log command to get logon events
def command = "wevtutil qe Security /q:\"*[System[(EventID=4624)]]\" /f:text /c:10"
def proc = command.execute()
def output = proc.text

// Parse each event entry
def entries = output.split("Event\\sRecord\\sID")
def formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

// Create Excel workbook
def workbook = new XSSFWorkbook()
def sheet = workbook.createSheet("Logon Events")

// Add header
def header = sheet.createRow(0)
["RecordID", "DateTime", "Account Name", "Logon Type"].eachWithIndex { val, idx ->
    header.createCell(idx).setCellValue(val)
}

int rowNum = 1

entries.each { entry ->
    def recordId = entry.find(/Record ID:\s+(\d+)/) { it[1] } ?: "N/A"
    def dateTime = entry.find(/Date:\s+([^\r\n]+)/) { it[1] } ?: "N/A"
    def account = entry.find(/Account Name:\s+([^\r\n]+)/) { it[1] } ?: "N/A"
    def logonType = entry.find(/Logon Type:\s+(\d+)/) { it[1] } ?: "N/A"

    def row = sheet.createRow(rowNum++)
    row.createCell(0).setCellValue(recordId.toString())
    row.createCell(1).setCellValue(dateTime)
    row.createCell(2).setCellValue(account)
    row.createCell(3).setCellValue(logonType)
}

// Write to Excel file
def fileOut = new FileOutputStream("LogonEvents.xlsx")
workbook.write(fileOut)
fileOut.close()
workbook.close()

println "✅ LogonEvents.xlsx has been created."
