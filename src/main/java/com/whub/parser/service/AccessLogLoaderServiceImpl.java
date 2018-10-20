package com.whub.parser.service;

import com.whub.parser.app.args.ParserArgs;
import org.springframework.stereotype.Service;

/**
 * parse the log and save into db
 */
@Service
public class AccessLogLoaderServiceImpl implements AccessLogLoaderService {

    @Override
    public void loadAccessLogs(ParserArgs parserArgs) {
        System.out.println(parserArgs.getAccessLogPath());
    }
}
