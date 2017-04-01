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
	.headerDefaultOverride() -- override header coming from datasource e.g. SQL source header will have columns names
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
	.dependsOn(job1)
	.dependsOn(job2)
	.runOnWeekends(true)
	.runOnUSHolidays(true)
	.runOnUKHolidays(true)
	.whenDependencyNotMet(devEmailConsumer,jsonConsumer)
	.whenFailed(devEmailConsumer,jsonConsumer)
	.whenDelayed()
	.whenCompleted()
	.alertIfNotCompleted()
	.retryDependencyCheckAfter(15)
	.maxWaitTimeForDependency()
	.frequency(DLY|MTH|EOM|QTR|Intraday|CronParser)
	.build()
	.schedule();
	
