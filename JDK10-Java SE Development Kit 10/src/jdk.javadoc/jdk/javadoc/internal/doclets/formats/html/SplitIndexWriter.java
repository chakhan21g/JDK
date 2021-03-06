/*
 * Copyright (c) 1998, 2017, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.formats.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTag;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlTree;
import jdk.javadoc.internal.doclets.formats.html.markup.Links;
import jdk.javadoc.internal.doclets.formats.html.markup.StringContent;
import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.util.DocFileIOException;
import jdk.javadoc.internal.doclets.toolkit.util.DocPath;
import jdk.javadoc.internal.doclets.toolkit.util.DocPaths;
import jdk.javadoc.internal.doclets.toolkit.util.IndexBuilder;


/**
 * Generate Separate Index Files for all the member names with Indexing in
 * Unicode Order. This will create "index-files" directory in the current or
 * destination directory and will generate separate file for each unicode index.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 *
 * @see java.lang.Character
 * @author Atul M Dambalkar
 * @author Bhavesh Patel (Modified)
 */
public class SplitIndexWriter extends AbstractIndexWriter {

    /**
     * Previous unicode character index in the built index.
     */
    protected int prev;

    /**
     * Next unicode character in the built index.
     */
    protected int next;

    private final List<Character> indexElements;

    /**
     * Construct the SplitIndexWriter. Uses path to this file and relative path
     * from this file.
     *
     * @param configuration the configuration for this doclet
     * @param path       Path to the file which is getting generated.
     * @param indexbuilder Unicode based Index from {@link IndexBuilder}
     * @param elements the collection of characters for which to generate index files
     * @param prev  the previous character that was indexed
     * @param next  the next character to be indexed
     */
    public SplitIndexWriter(HtmlConfiguration configuration,
                            DocPath path,
                            IndexBuilder indexbuilder,
                            Collection<Character> elements,
                            int prev, int next) {
        super(configuration, path, indexbuilder);
        this.indexElements = new ArrayList<>(elements);
        this.prev = prev;
        this.next = next;
    }

    /**
     * Generate separate index files, for each Unicode character, listing all
     * the members starting with the particular unicode character.
     *
     * @param configuration the configuration for this doclet
     * @param indexbuilder IndexBuilder built by {@link IndexBuilder}
     * @throws DocFileIOException if there is a problem generating the index files
     */
    public static void generate(HtmlConfiguration configuration,
                                IndexBuilder indexbuilder) throws DocFileIOException {
        DocPath path = DocPaths.INDEX_FILES;
        Set<Character> keys = new TreeSet<>(indexbuilder.getIndexMap().keySet());
        keys.addAll(configuration.tagSearchIndexKeys);
        ListIterator<Character> li = new ArrayList<>(keys).listIterator();
        int prev;
        int next;
        while (li.hasNext()) {
            prev = (li.hasPrevious()) ? li.previousIndex() + 1 : -1;
            Object ch = li.next();
            next = (li.hasNext()) ? li.nextIndex() + 1 : -1;
            DocPath filename = DocPaths.indexN(li.nextIndex());
            SplitIndexWriter indexgen = new SplitIndexWriter(configuration,
                    path.resolve(filename),
                    indexbuilder, keys, prev, next);
            indexgen.generateIndexFile((Character) ch);
            if (!li.hasNext()) {
                indexgen.createSearchIndexFiles();
            }
        }
    }

    /**
     * Generate the contents of each index file, with Header, Footer,
     * Member Field, Method and Constructor Description.
     *
     * @param unicode Unicode character referring to the character for the
     * index.
     * @throws DocFileIOException if there is a problem generating an index file
     */
    protected void generateIndexFile(Character unicode) throws DocFileIOException {
        String title = configuration.getText("doclet.Window_Split_Index",
                unicode.toString());
        HtmlTree body = getBody(true, getWindowTitle(title));
        HtmlTree htmlTree = (configuration.allowTag(HtmlTag.HEADER))
                ? HtmlTree.HEADER()
                : body;
        addTop(htmlTree);
        addNavLinks(true, htmlTree);
        if (configuration.allowTag(HtmlTag.HEADER)) {
            body.addContent(htmlTree);
        }
        HtmlTree divTree = new HtmlTree(HtmlTag.DIV);
        divTree.setStyle(HtmlStyle.contentContainer);
        addLinksForIndexes(divTree);
        if (configuration.tagSearchIndexMap.get(unicode) == null) {
            addContents(unicode, indexbuilder.getMemberList(unicode), divTree);
        } else if (indexbuilder.getMemberList(unicode) == null) {
            addSearchContents(unicode, configuration.tagSearchIndexMap.get(unicode), divTree);
        } else {
            addContents(unicode, indexbuilder.getMemberList(unicode),
                    configuration.tagSearchIndexMap.get(unicode), divTree);
        }
        addLinksForIndexes(divTree);
        body.addContent((configuration.allowTag(HtmlTag.MAIN)) ? HtmlTree.MAIN(divTree) : divTree);
        if (configuration.allowTag(HtmlTag.FOOTER)) {
            htmlTree = HtmlTree.FOOTER();
        }
        addNavLinks(false, htmlTree);
        addBottom(htmlTree);
        if (configuration.allowTag(HtmlTag.FOOTER)) {
            body.addContent(htmlTree);
        }
        printHtmlDocument(null, true, body);
    }

    /**
     * Add links for all the Index Files per unicode character.
     *
     * @param contentTree the content tree to which the links for indexes will be added
     */
    protected void addLinksForIndexes(Content contentTree) {
        for (int i = 0; i < indexElements.size(); i++) {
            int j = i + 1;
            contentTree.addContent(Links.createLink(DocPaths.indexN(j),
                    new StringContent(indexElements.get(i).toString())));
            contentTree.addContent(Contents.SPACE);
        }
    }

    /**
     * Get link to the previous unicode character.
     *
     * @return a content tree for the link
     */
    @Override
    public Content getNavLinkPrevious() {
        Content prevletterLabel = contents.prevLetter;
        if (prev == -1) {
            return HtmlTree.LI(prevletterLabel);
        }
        else {
            Content prevLink = Links.createLink(DocPaths.indexN(prev),
                    prevletterLabel);
            return HtmlTree.LI(prevLink);
        }
    }

    /**
     * Get link to the next unicode character.
     *
     * @return a content tree for the link
     */
    @Override
    public Content getNavLinkNext() {
        Content nextletterLabel = contents.nextLetter;
        if (next == -1) {
            return HtmlTree.LI(nextletterLabel);
        }
        else {
            Content nextLink = Links.createLink(DocPaths.indexN(next),
                    nextletterLabel);
            return HtmlTree.LI(nextLink);
        }
    }
}
