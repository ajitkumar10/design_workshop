

SourceReader 		==> DataStager/Cache 		==> DataTransformer 	==> TargetWriter 	==> Delivery 
(SQL,DB,FlatFile)	  (Read into local objects)	(Business Rules)	(CSV,XLS,PDF,Delimited)	   (Email,FTP,SFTP)

Monitoring and Alerts
(Events generated at each stage)

ReportConfig
	.id()
	.rptName("report {report_type} - {report_sub_type}")
	.rptOutputDir()	
	.rptOutputFileName(r_openorder_ssic_$date.csv)
	.rptOutputFileDaysToKeep(5)
	.rptInputDataSource(
		new SqlSource(),
		-- or new TextFileSource()
		-- or new DBSource()
		)
	.headerDefaultOverride() -- override header coming from datasource e.g. default header will be column names for SQL source
	.headerOn()
	.footerDefaultOverride() -- default is TLR|{record_Count}
	.footerOn
	.repeatHeaderFooterAfterLines()
	.output(
		new OutputConfig()
		.format(delimited(txt)|csv|xls|xlsx|pdf)
		.delimiter(',')
		.build()
	)
	.compress()
	.rptLogDir()
	.rptLogFileName()
	.rptLogDaysToKeep()
	.build(); --calls validate, use validate to test config

JobScheduler
	.jobId()
	.dependsOn(job1, job2)
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
	
