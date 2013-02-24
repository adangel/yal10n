package net.sf.yal10n.analyzer;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.logging.Log;

/**
 * Simple null logger for maven during tests.
 */
public class NullLog implements Log
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDebugEnabled()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug( CharSequence content )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug( CharSequence content, Throwable error )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug( Throwable error )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInfoEnabled()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info( CharSequence content )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info( CharSequence content, Throwable error )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info( Throwable error )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWarnEnabled()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn( CharSequence content )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn( CharSequence content, Throwable error )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn( Throwable error )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isErrorEnabled()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error( CharSequence content )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error( CharSequence content, Throwable error )
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error( Throwable error )
    {
        // do nothing
    }
}
