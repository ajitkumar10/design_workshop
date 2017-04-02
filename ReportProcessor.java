

SourceReader 		==> DataStager/Cache 		==> DataTransformer 	==> TargetWriter 	==> Delivery 
(SQL,DB,FlatFile)	  (Read into local objects)	(Business Rules)	(CSV,XLS,PDF,Delimited)	   (Email,FTP,SFTP)

Monitoring and Alerts
(Events generated at each stage)

ReportConfig
	.id()
	.rptName("report {report_type} - {report_sub_type}")
	.rptRunDate(LocalDate.of(2017,03,22))
	.rptInputStartDate()
	.rptInputEndDate()
	.rptFrequency(DAILY|INTRADAY|WEEKLY|MONTHLY|QUARTERLY|YEALRY|CUSTOM)
	# .rptIntradayRunTime(LocalTime.of(09,30), LocalTime.of(16,00)) -- only required for intraday reports
	.rptOutputDir()	
	.rptOutputFileNames(r_{report_type}_{report_sub_type}_{input_date(s)}.csv)  -- if multiple files, delivered in order specified
	.rptDoneFileRequired(true)
	.rptDoneFileName('{report_client}.done')
	.rptOutputFileDaysToKeep(5)
	.rptInputDataSource(
		new OracleSource(),
		-- or new TextFileSource()
		-- or new SybaseSource()
		)
	.headerDefaultOverride() -- override header coming from datasource e.g. default header will be column names for SQL source
	.headerOn()
	.footerDefaultOverride() -- default is TLR|{record_Count}
	.footerOn
	.repeatHeaderFooterAfterLines()
	.rptDataTransformer(
		-- pipeline of transformers
		new DelimitedTextTransformer("|").andThen(new PythonCustomClientFormatter())
		new OutputConfig()
		.format(delimited(txt)|csv|xls|xlsx|pdf)
		.delimiter(',')
		.build()
	)
	.email(
 		new EmailConfig()
 		.fromAddr()
 		.toAddr()
 		.ccAddr()
 		.bccAddr()
 		.subject()  -- Append Env 
 		.body()
 		.attachments(defaults to reportFileName)
 		.build()
 		
 		--more email configs, if same report needs to be sent with different subject/body or for privacy
 		)
 	.ftp (
 		new FtpConfig()
 		 .ftpHost()
 		 .ftpUser()
 		 .ftpPort()
 		 .ftpPasswd()
 		 .ftpDir()
 		 .ftpFileName(defaults to reportFileName)
 		 .retryAfter(15, TimeUnit.MINUTES)
 		 .retryAttempts(-1) -1 = never
 		 .build(),
 		 
 		 -- more ftp configs
 		)
 	.sftp (
 		new SftpConfig()
 		 .sftpHost()
 		 .sftpUser()
 		 .sftpPassword()
		 .sftpIdentityFilePath()  -- either password is required or identity file path 
 		 .sftpPort()
 		 .sftpDir()
 		 .sftpFileName(defaults to reportFileName)
 		 .build()
 		 
 		 -- more sftp configs
 		)
	.compress()
	.rptLogDir()
	.rptLogFileName()
	.rptLogDaysToKeep()
	.build(); --calls validate, use validate directly to test config

ReportScheduler
	.rptId()
	.dependsOn(job1, job2)  -- takes a list of predicates and tests them each time e.g. new StgLoadStatusPredicate(101)
	.runOnWeekends(true)
	.runOnUSHolidays(false)
	.runOnUKHolidays(true)
	.whenStarted(devEmailConsumer,jsonConsumer)
	.whenDependencyNotMet(devEmailConsumer,jsonConsumer)
	.retryDependencyCheckAfter(15, TimeUnit.MINUTES)
	.maxWaitTimeForDependency(LocalDateTime.of(rptRunDate, LocalTime.of(23,0)))  -- 11:00 PM of report run date  
	.whenFailed(devEmailConsumer,jsonConsumer)
	.whenDelayed(devEmailConsumer,jsonConsumer)
	.whenCompleted(devEmailConsumer,jsonConsumer)
	.frequency(DLY|MTH|EOM|QTR|Intraday|CronParser)
	.build()
	.schedule();
	
