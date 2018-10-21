package com.whub.parser.service;

import com.whub.parser.app.args.ParserArgs;

public interface AccessLogLoaderService {
    /**
     * parse and save access logs in to DB
     */
    public void loadAccessLogs(ParserArgs parserArgs) throws Exception;
}
