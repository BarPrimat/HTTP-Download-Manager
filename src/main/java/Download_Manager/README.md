# Java classes explanations
- `IdcDm` - This class is the main class for HTTP-Download-Manager the class received URL or list od URL and number of thread and start downloading 
	from the given input.
- `ConnectionDownloads` - This class is Runnable thread for download the data from the given URL in the range that the creator given to this thread.  
- `DataChunk` - This class define a chunk.

- `ManagerDownloader` - This class is downloading file from list of URL in to file that call fileName with the suffix using the given number of the threads.
- `MetadataFile` - This class represents the metadata object that will write in to the disk with the name of the file and the TEMP_SUFFIX.
- `WriterManager` - This class responsible for writing the data and tge temporary metadata file on the disk
		and transfer the 2 temporary metadat file in to 1 (with ATOMIC_MOVE) that for saving the metadata file in case of
		problem with writing on the disk
- `StaticVariable` -  This class represents a static variable
