package com.vs2010wy.tool.ui;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.EditorLinePainter;
import com.intellij.openapi.editor.LineExtensionInfo;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class CommentPainter extends EditorLinePainter {
    TextAttributes commentAttributes;
    public CommentPainter(){
        super();
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();

        // 获取注释的 TextAttributesKey
        TextAttributesKey commentAttributesKey = TextAttributesKey.find("JAVA_LINE_COMMENT");

        // 获取注释的文本属性
        commentAttributes = scheme.getAttributes(commentAttributesKey);

    }

    @Override
    public @Nullable Collection<LineExtensionInfo> getLineExtensions(@NotNull Project project, @NotNull VirtualFile file, int lineNumber) {
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Bookmark bookmark = bookmarkManager.findEditorBookmark(FileDocumentManager.getInstance().getDocument(file), lineNumber);
        ArrayList<LineExtensionInfo> arrayList = new ArrayList<LineExtensionInfo>();
        if(bookmark == null){
            arrayList.add(new LineExtensionInfo("//bookmark.getDescription()",commentAttributes));
        }

        return arrayList;
    }
}
