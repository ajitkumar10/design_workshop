ReportConfig
	.id()
	.rptName("report {report_type} - {report_sub_type}")
	.email(
		new EmailConfig()
		.fromEmail()
		.toEmail()
		.ccEmail()
		.bccEmail()
		.subject()  -- Append Env 
		.body()
		.attachments(defaults to reportFileName)
		.build(),
		
		--more email configs
		)
	.ftp (
		new FtpConfig()
		 .ftpHost()
		 .ftpUser()
		 .ftpPort()
		 .ftpPasswd()
		 .ftpDir()
		 .ftpFileName(defaults to reportFileName)
		 .retryAfter(15 min)
		 .retryAttempts(-1) never
		 .build(),
		 
		 -- more ftp configs
		)
	.sftp (
		new SftpConfig()
		 .sftpHost()
		 .sftpUser()
		 .sftpPassword()
		 .sftpPort()
		 .sftpDir()
		 .sftpFileName(defaults to reportFileName)
		 .build(),
		 
		 -- more sftp configs
		)
	.rptDir()	
	.rptFile(r_openorder_ssic_$date.csv)
	.rptDaysToKeep(5)
	.dataSource(
		new SqlSource(),
		-- or new TextFileSource()
		-- or new DBSource()
		)
	.headerOverride() -- override header coming from datasource	
	.headerOn()
	.footerOverride()
	.footerOn
	.repeatHeaderFooterAfterLines()
	.output(
		new OutputConfig()
		.format(delimited(txt)|csv|xls|xlsx|pdf)
		.delimiter(',')
		.build()
	)
	.compress()
	.logDir()
	.logFileName()
	.logDaysToKeep()
	.build();

JobScheduler
	.jobId()
	.dependsOn(job1)
	.dependsOn(job2)
	.runOnWeekends(true)
	.runOnUSHolidays(true)
	.runOnUKHolidays(true)
	.whenDependencyNotMet(devEmailConsumer,jsonConsumer)
	.whenFailed(devEmailConsumer,jsonConsumer)
	.alertIfNotCompleted
	.retryDependencyCheckAfter(15)
	.maxWaitTimeForDependency()
	.frequency(DLY|MTH|EOM|QTR|Intraday|CronParser)
	.build()
	.run();
	
