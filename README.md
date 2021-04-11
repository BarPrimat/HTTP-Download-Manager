# HTTP-Download-Manager
This project developed with java, java swing for GUI and JUnit for Unit test.  
The project allows as to download files via URL link or by multi url address (if there is) with parallel connection using multi threads.  
You can run with the GUI:  
<img src="https://github.com/BarPrimat/HTTP-Download-Manager/blob/master/DownloadManagerPic2.jpg" width="400" height="250" />  
Or you can run this project with the CLI as following:  
`java IdcDm <URL|URL-LIST-FILE> <MAX-CONCURRENT-CONNECTIONS>`  
for example:  
1. `java IdcDm https://archive.org/download/Mario1_500/Mario1_500.avi 8`  
2. `java IdcDm CentOS-6.10-x86_64-netinstall.iso.list 5`

- #### [Java classes explanations](https://github.com/BarPrimat/HTTP-Download-Manager/blob/master/src/main/java/Download_Manager/README.md)
- #### [Go to Unit testing](https://github.com/BarPrimat/HTTP-Download-Manager/tree/master/src/test/java/Download_Manager)
