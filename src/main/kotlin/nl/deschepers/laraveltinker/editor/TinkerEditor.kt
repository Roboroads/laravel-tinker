package nl.deschepers.laraveltinker.editor

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.testFramework.LightVirtualFile
import com.jetbrains.php.lang.PhpFileType


class TinkerEditor(project: Project) {
    var fileEditorManager: FileEditorManager = FileEditorManager.getInstance(project)
    var fileFactory: PsiFileFactory = PsiFileFactory.getInstance(project)
    var editorFactory: EditorFactory = EditorFactory.getInstance()
    val tinkerDocument: Document

    init {
        val content = "<?php\n// Tinker away!";
        tinkerDocument = editorFactory.createDocument(content);
        val lvf = LightVirtualFile("Tinker Console", PhpFileType.INSTANCE, content)
        fileEditorManager.openFile(lvf, true)
    }


}