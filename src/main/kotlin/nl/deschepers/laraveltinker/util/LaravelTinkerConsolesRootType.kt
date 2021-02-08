package nl.deschepers.laraveltinker.util

import com.intellij.icons.AllIcons
import com.intellij.ide.scratch.RootType
import com.intellij.ide.scratch.ScratchFileService
import com.intellij.lang.Language
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.LayeredIcon
import com.intellij.ui.UIBundle
import com.intellij.util.ObjectUtils
import com.jetbrains.php.lang.PhpLanguage
import nl.deschepers.laraveltinker.Strings
import java.io.IOException
import javax.swing.Icon

/**
 * Most content is copied from ScratchRootType - but that class is final so I can't extend it.
 * @see com.intellij.ide.scratch.ScratchRootType
 */
class LaravelTinkerConsolesRootType : RootType("laravel-tinker", Strings.get("lt.console.menu_title")) {
    companion object {
        fun getInstance(): LaravelTinkerConsolesRootType {
            return findByClass(LaravelTinkerConsolesRootType::class.java)
        }
    }

    override fun substituteLanguage(project: Project, file: VirtualFile): Language? {
        return ScratchFileService.getInstance().scratchesMapping.getMapping(file)
    }

    override fun substituteIcon(project: Project, file: VirtualFile): Icon? {
        if (file.isDirectory) return null
        val icon = ObjectUtils.notNull(super.substituteIcon(project, file), AllIcons.FileTypes.Text)
        return LayeredIcon.create(icon, AllIcons.Actions.Scratch)
    }

    fun createScratchFile(project: Project?, text: String?, option: ScratchFileService.Option?): VirtualFile? {
        val fileName = Strings.get("lt.console.name")
        return try {
            WriteCommandAction.writeCommandAction(project).withName(UIBundle.message("file.chooser.create.new.scratch.file.command.name"))
                .withGlobalUndo().shouldRecordActionForActiveDocument(false)
                .withUndoConfirmationPolicy(UndoConfirmationPolicy.REQUEST_CONFIRMATION).compute<VirtualFile, IOException> {
                    val fileService = ScratchFileService.getInstance()
                    val file = fileService.findFile(this, fileName, option!!)
                    // save text should go before any other manipulations that load document,
                    // otherwise undo will be broken
                    VfsUtil.saveText(file, text!!)
                    fileService.scratchesMapping.setMapping(file, PhpLanguage.INSTANCE)
                    file
                }
        } catch (e: IOException) {
            Messages.showMessageDialog(
                UIBundle.message(
                    "create.new.file.could.not.create.file.error.message",
                    fileName
                ),
                UIBundle.message("error.dialog.title"),
                Messages.getErrorIcon()
            )
            null
        }
    }
}
