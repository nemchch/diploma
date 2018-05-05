@echo off
setlocal
for /f "skip=8 tokens=2,3,4,5,6,7,8 delims=: " %%D in ('robocopy /l * \ \ /ns /nc /ndl /nfl /np /njh /XF * /XD *') do (
 set "dow=%%D"
 if "%%E" EQU "Jan" (set "month=01")
 if "%%E" EQU "Feb" (set "month=02")
 if "%%E" EQU "Mar" (set "month=03")
 if "%%E" EQU "Apr" (set "month=04")
 if "%%E" EQU "May" (set "month=05")
 if "%%E" EQU "Jun" (set "month=06")
 if "%%E" EQU "Jul" (set "month=07")
 if "%%E" EQU "Aug" (set "month=08")
 if "%%E" EQU "Sep" (set "month=09")
 if "%%E" EQU "Oct" (set "month=10")
 if "%%E" EQU "Nov" (set "month=11")
 if "%%E" EQU "Dec" (set "month=12")
 set "day=%%F"
 set "HH=%%G"
 set "MM=%%H"
 set "SS=%%I"
 set "year=%%J"
)
set "date=%year%.%month%.%day%_%HH%.%MM%.%SS%"

C:\Windows\System32\telnet localhost 3345 -f C:\logs\test_%date%.log